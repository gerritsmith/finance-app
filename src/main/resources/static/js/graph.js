if (false) {
  let d3 = require('./external/d3/d3.js');
}


function checkValidChoices() {

  let columnName = document.getElementById("columnName").value;
  let denominatorName = document.getElementById("denominatorName").value;

  if (columnName !== denominatorName) {
    selectDataColumnAndDrawCharts();
    d3.select("#choose-dependent-variable p")
      .remove();
  } else {
    d3.select("#choose-dependent-variable")
      .append("p")
      .attr("class", "error")
      .text("Can't plot quantity with respect to itself!");
  }

}


function selectDataColumnAndDrawCharts() {

  let columnName = document.getElementById("columnName").value;
  let denominatorName = document.getElementById("denominatorName").value;

  let columnDisplayNames = dataTable.columnDisplayNames;
  let denominatorDisplayNames = dataTable.denominatorDisplayNames;

  let data = dataTable.map(r => {
    let value = r[columnName]/(denominatorName === 'one' ? 1 : r[denominatorName]);
    value = Number.isNaN(value) || !Number.isFinite(value) ? undefined : value;
    return {
      date: r.temporal,
      value: value
    };
  });

  // Axes label string
  if (denominatorName === 'one') {
    data.valueLabel = columnDisplayNames[columnName];
  } else {
    data.valueLabel = columnDisplayNames[columnName] + ' per ' + denominatorDisplayNames[denominatorName];
  }
  
  // Remove old svg images if they exist
  d3.select("#line-plot svg").remove();
  d3.select("#bar-plot svg").remove();
  d3.select("#histogram svg").remove();

  drawCharts(data);

}



function drawCharts(data) {

  // Dependent Variable Name
  let valueLabel = data.valueLabel;

  // Parse date string, sort by date, remove extra fields
  data = parseInputDateStrings(data);

  // Default Chart Size
  let width = 1000;
  let height = 500;
  let margin = {
    top: 40,
    right: 20,
    bottom: 50,
    left: 60
  };

  drawLineGraph(data.slice());
  drawBarPlot(data.slice());
  drawHistogram(data.slice());

  /********************************************************
   * Draw line graph
   */
  function drawLineGraph(data) {

    // Add missing dates to array with undefined values
    // data = addMissingDates(data);

    // Make axes scale coordinate transformations
    let x = d3.scaleTime()
              .domain(d3.extent(data, d => d.date))
              .range([margin.left, width - margin.right]);
    let y = d3.scaleLinear()
              .domain([0, d3.max(data, d => d.value)]).nice()
              .range([height - margin.bottom, margin.top]);

    // Make axes svg groups
    let xAxis = g => g.attr("transform", `translate(0,${height - margin.bottom})`)
                      .call(d3.axisBottom(x)
                              .ticks(width / 80)
                              .tickSizeOuter(0));
    let yAxis = g => g.attr("transform", `translate(${margin.left},0)`)
                      .call(d3.axisLeft(y));


    // Make path builder
    let line = d3.line()
                 .defined(d => !isNaN(d.value))
                 .x(d => x(d.date))
                 .y(d => y(d.value));

    // Make chart
    const svg = addSVGToElement("#line-plot", width, height, xAxis, yAxis, valueLabel);

    svg.append("path")
       .datum(data)
       .attr("fill", "none")
       .attr("stroke", "steelblue")
       .attr("stroke-width", 1.5)
       .attr("stroke-linejoin", "round")
       .attr("stroke-linecap", "round")
       .attr("d", line);
    
    svg.append("g")
       .attr("fill", "steelblue")
       .selectAll("circle")
       .data(data.filter(d => !isNaN(d.value)))
       .enter()
       .append("circle")
        .attr("cx", d => x(d.date))
        .attr("cy", d => y(d.value))
        .attr("r", 5);

    //////////////////////////////////////////////
    // Add mouseover tooltips
    svg.append("g")
       .attr("id", "tooltip");

    svg.on("touchmove mousemove", function () {
      const {date, value} = bisect(d3.mouse(this)[0], x, data);
      highlightDataPoint(date, value);
    });

    svg.on("touchend mouseleave", () => {
      removeHighlight();
    });

  }


  /****************************************************
   * Draw bar plot
   */
  function drawBarPlot(data) {

    // // Add missing dates to array with undefined values
    // data = addMissingDates(data);

    // Make axes scale coordinate transformations
    let x = d3.scaleBand()
              .domain(d3.range(data.length))
              .range([margin.left, width - margin.right])
              .padding(0.1);
    let y = d3.scaleLinear()
              .domain([0, d3.max(data, d => d.value)]).nice()
              .range([height - margin.bottom, margin.top]);

    // Make axes groups
    let format = d3.timeFormat("%b %e")
    let xAxis = g => g.attr("transform", `translate(0,${height - margin.bottom})`)
                      .call(d3.axisBottom(x)
                              .tickValues(x.domain().filter(i => i % (Math.round(data.length/15)) === 0))
                              .tickFormat(i => format(data[i].date))
                              .tickSizeOuter(0));
    let yAxis = g => g.attr("transform", `translate(${margin.left},0)`)
                      .call(d3.axisLeft(y));

    // Make chart
    const svg = addSVGToElement("#bar-plot", width, height, xAxis, yAxis, valueLabel);

    svg.append("g")
      .attr("fill", "steelblue")
      .selectAll("rect")
        .data(data)
        .join("rect")
        .attr("x", (d, i) => x(i))
        .attr("y", d => d.value == undefined ? y(0) : y(d.value))
        .attr("height", d => d.value == undefined ? 0 : y(0) - y(d.value))
        .attr("width", x.bandwidth())
        .on("touchmove mousemove", function(d) {
          highlightDataPoint(d.date, d.value);
        })
        .on("touchend mouseleave", () => {
          removeHighlight();
        });

  }


  /****************************************************
   * Draw histogram
   */
  function drawHistogram(data) {

    // Remove dates
    data = data.map(d => d.value);

    // Make x scaler
    let [xMin, xMax] = d3.extent(data);
    xMax = xMax + 1;
    let x = d3.scaleLinear()
              .domain([xMin < 0 ? xMin : 0, xMax]).nice()
              .range([margin.left, width - margin.right]);
    // Sort into bins
    let binThresholds = x.ticks(Math.pow(data.length, 0.66));  // x.ticks(40)  // x.ticks(10)
    let binDivider = d3.histogram()
                       .domain(x.domain())
                       .thresholds(binThresholds);  

    let bins = binDivider(data);
    // Make y scaler
    let y = d3.scaleLinear()
              .domain([0, d3.max(bins, d => d.length)]).nice()
              .range([height - margin.bottom, margin.top]);

    // Make axis groups
    let xAxis = g => g.attr("transform", `translate(0,${height - margin.bottom})`)
                      .call(d3.axisBottom(x)
                              .ticks(Math.min(15, binThresholds.length))    // .ticks(width/80)
                              .tickSizeOuter(0))
                      .call(g => g.append("g")
                                  .attr("transform", `translate(${width / 2},0)`)
                                  .attr("class", "axis-label")
                                  .append("text")
                                  .attr("fill", "currentColor")
                                  .attr("y", 25)
                                  .attr("dy", "0.71em")
                                  .attr("text-anchor", "middle")
                                  .attr("font-weight", "bold")
                                  .text(valueLabel));
    
    let yAxis = g => g.attr("transform", `translate(${margin.left},0)`)
                      .call(d3.axisLeft(y)
                              .ticks(Math.min(height/40, y.domain()[1])))
                      .call(g => g.append("g")
                                  .attr("transform", `translate(0,${height / 2}) rotate(-90)`)
                                  .attr("class", "axis-label")
                                  .append("text")
                                  .attr("fill", "currentColor")
                                  .attr("y", -20)
                                  .attr("dy", "-0.71em")
                                  .attr("text-anchor", "middle")
                                  .attr("font-weight", "bold")
                                  .text("Count"));

    // Make chart
    const svg = addSVGToElement("#histogram", width, height, xAxis, yAxis);

    svg.append("g")
       .attr("fill", "steelblue")
       .selectAll("rect")
        .data(bins)
        .join("rect")
        .attr("x", d => x(d.x0) + 1)
        .attr("width", d => Math.max(0, x(d.x1) - x(d.x0) - 1))
        .attr("y", d => y(d.length))
        .attr("height", d => y(0) - y(d.length));

  }

}


/** MOUSE INTERACTION
 * Highlight in all charts the data point under the mouse
 */
function highlightDataPoint(date, value) {
  if (value != undefined) {
    const activeCircle = d3.selectAll("#line-plot svg g circle")
                           .attr("fill", null)
                           .filter(d => d.date === date)
                           .attr("fill", "red");

    d3.select("#tooltip")
      .attr("transform", `translate(${activeCircle.attr("cx")},${activeCircle.attr("cy")})`)
      .call(callout, `${value.toLocaleString(undefined, {maximumFractionDigits: 2})}
${date.toLocaleString(undefined, {month: "short", day: "numeric", year: "numeric"})}`);

    d3.selectAll("#bar-plot svg g rect")
      .attr("fill", null)
      .filter(d => d.date === date)
      .attr("fill", "red");
      
    d3.selectAll("#histogram svg g rect")
      .attr("fill", null)
      .filter(d => (d.x0 <= value && value < d.x1))
      .attr("fill", "red");
  }
}

/** MOUSE INTERACTION
 * Remove all highlights applied to charts when mouse leaves svg
 */
function removeHighlight() {
  d3.select("#tooltip")
    .call(callout, null);
  d3.selectAll("circle")
    .attr("fill", null);
  d3.selectAll("rect")
    .attr("fill", null);
}


/** MOUSE INTERACTION
 * Build or hide the tooltip for the line graph
 */
let callout = (g, value) => {
  if (!value) {
    return g.style("display", "none");
  }
  g.style("display", null)
   .style("pointer-events", "none")
   .style("font", "1em sans-serif");
  const path = g.selectAll("path")
                .data([null])
                .join("path")
                .attr("fill", "white")
                .attr("stroke", "black");
  const text = g.selectAll("text")
                .data([null])
                .join("text")
                .call(text => text.selectAll("tspan")
                                  .data((value + "").split(/\n/))
                                  .join("tspan")
                                  .attr("x", 0)
                                  .attr("y", (d, i) => `${i * 1.25}em`)
                                  .style("font-weight", (_, i) => i ? null : "bold")
                                  .text(d => d));
  const {x, y, width: w, height: h} = text.node().getBBox();

  let parentSVG = g.select(function() {
    return this.parentNode;
  });
  let height = Number(parentSVG.attr("viewBox").split(',')[3])

  if (g.attr("transform").match(/\(([^)]+)\)/)[1].split(',').map(Number)[1] < 0.5 * height) {
    text.attr("transform", `translate(${-w / 2},${0.8 * h})`);
    path.attr("d", `M${-w / 2 - 10},5H-5l5,-5l5,5H${w / 2 + 10}v${h + 20}h-${w + 20}z`);
  } else {
    text.attr("transform", `translate(${-w / 2},${-h})`);
    path.attr("d", `M${-w / 2 - 10},-5H-5l5,5l5,-5H${w / 2 + 10}v-${h + 20}h-${w + 20}z`);
  }
};


/** MOUSE INTERACTION
 * Determine the data point over which the mouse is hovering
 *  for a chart with dates along the x-axis
 */
let bisect = (mouseX, xScale, data) => {
  const bisect = d3.bisector(d => d.date).left;
  const date = xScale.invert(mouseX);
  const index = bisect(data, date, 1);
  const a = data[index - 1];
  const b = data[index];
  if (b == undefined) {
    return a;
  }
  return date - a.date > b.date - date ? b : a;
}


/** SVG TEMPLATE
 * Add SVG element with given x axis group and y axis group
 *  to the element with the given id
 */
function addSVGToElement(id, width, height, xAxis, yAxis, title) {
  const svg = d3.select(id)
                .append("svg")
                .attr("viewBox", [0, 0, width, height]);
  svg.append("g").call(xAxis);
  svg.append("g").call(yAxis);
  if (title != undefined) {
    svg.append("g")
    .attr("transform", `translate(${width / 2},0)`)
    .attr("class", "axis-label")
    .append("text")
    .attr("fill", "currentColor")
    .attr("dy", "0.75em")
    .attr("text-anchor", "middle")
    .attr("font-weight", "bold")
    .text(title);
  }
  return svg;
}


/** FORMAT DATA
 * Parse input data array date strings into Date objects
 * Sorts the array by date
 * Will remove any extra fields on the data array object
 */
function parseInputDateStrings(data) {
  return data.map(d => {
    let p = d.date.split("-");
    return {
      date: new Date(p[0], p[1]-1, p[2]),
      value: d.value
    };
  }).sort((a, b) => d3.ascending(a.date, b.date));
}

/** FORMAT DATA
 * Add missing dates to data array with corresponding values undefined
 * Also adds extra day at start and end for padding the graph
 */
function addMissingDates(data) {
  let [minDate, maxDate] = d3.extent(data, d => d.date);

  // add additional day before min to pad the graph
  let date = new Date(minDate.getTime());
  date.setDate(date.getDate() - 1);
  // add additional day after max in loop condition
  let upperLimit = new Date(maxDate.getTime());
  upperLimit.setDate(upperLimit.getDate() + 1);

  let newData = [];
  while (date <= upperLimit) {
    if (data.length > 0 && data[0].date - date === 0) {
      newData.push(data.shift());
    } else {
      newData.push({
        date: new Date(date.getTime()),
        value: undefined
      });
    }
    date.setDate(date.getDate() + 1);
  }
  return newData;
}
