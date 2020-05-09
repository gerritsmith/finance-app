// const d3 = require("d3");


/****************************************************
 * Draw histogram
 */
function drawHistogram(data) {
  
  let valueLabel = data.valueLabel;

  // Remove dates
  data = data.map(d => d.value);

  // Set chart size
  let width = 1000;
  let height = 500;
  let margin = {
    top: 20,
    right: 30,
    bottom: 30,
    left: 40
  };

  // Make x scaler
  let [xMin, xMax] = d3.extent(data);
  xMax = xMax + 1;
  let x = d3.scaleLinear()
            .domain([xMin, xMax]).nice()
            .range([margin.left, width - margin.right]);

  // Sort into bins
  let binDivider = d3.histogram()
                     .domain(x.domain())
                     .thresholds(x.ticks(40));
  let bins = binDivider(data);

  // Make y scaler
  let y = d3.scaleLinear()
            .domain([0, d3.max(bins, d => d.length)]).nice()
            .range([height - margin.bottom, margin.top]);

  // Make axis groups
  let xAxis = g => g.attr("transform", `translate(0,${height - margin.bottom})`)
                    .call(d3.axisBottom(x)
                            .ticks(width / 80)
                            .tickSizeOuter(0))
                    .call(g => g.append("text")
                                .attr("x", width - margin.right)
                                .attr("y", -4)
                                .attr("fill", "currentColor")
                                .attr("font-weight", "bold")
                                .attr("text-anchor", "end")
                                .text(valueLabel))

  let yAxis = g => g.attr("transform", `translate(${margin.left},0)`)
                    .call(d3.axisLeft(y)
                            .ticks(height / 40))
                    .call(g => g.select(".tick:last-of-type text").clone()
                                .attr("x", 4)
                                .attr("text-anchor", "start")
                                .attr("font-weight", "bold")
                                .text("Count"))

  // Make chart
  const svg = d3.select("#histogram")
                .append("svg")
                .attr("viewBox", [0, 0, width, height]);

  svg.append("g")
     .attr("fill", "steelblue")
     .selectAll("rect")
      .data(bins)
      .join("rect")
       .attr("x", d => x(d.x0) + 1)
       .attr("width", d => Math.max(0, x(d.x1) - x(d.x0) - 1))
       .attr("y", d => y(d.length))
       .attr("height", d => y(0) - y(d.length));

  svg.append("g")
     .call(xAxis);

  svg.append("g")
     .call(yAxis);

}


/****************************************************
 * Draw bar plot
 */
function drawBarPlot(data) {

  let valueLabel = data.valueLabel;

  // Parse date string
  data = parseInputDateStrings(data);

  // // Add missing dates to array with undefined values
  // data = addMissingDates(data);

  // Set chart size
  let width = 1000;
  let height = 500;
  let margin = {
    top: 20,
    right: 30,
    bottom: 30,
    left: 40
  };

  // Make axes scales
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
                            .tickFormat(i => format(data[i].date))
                            .tickSizeOuter(0));

  let yAxis = g => g.attr("transform", `translate(${margin.left},0)`)
                    .call(d3.axisLeft(y))
                    .call(g => g.append("text")
                                .attr("x", -margin.left)
                                .attr("y", 10)
                                .attr("fill", "currentColor")
                                .attr("text-anchor", "start")
                                .text(valueLabel));

  // Make line
  let line = d3.line()
                .defined(d => !isNaN(d.value))
                .x(d => x(d.date))
                .y(d => y(d.value));

  // Make chart
  const svg = d3.select("#bar-plot")
                .append("svg")
                .attr("viewBox", [0, 0, width, height]);
  
  svg.append("g")
      .call(xAxis);

  svg.append("g")
      .call(yAxis);

  svg.append("g")
     .attr("fill", "steelblue")
     .selectAll("rect")
      .data(data)
      .join("rect")
       .attr("x", (d, i) => x(i))
       .attr("y", d => y(d.value))
       .attr("height", d => y(0) - y(d.value))
       .attr("width", x.bandwidth());

}


/********************************************************
 * Draw line graph
 */
function drawLineGraph(data) {

  let valueLabel = data.valueLabel;

  // Parse date string
  data = parseInputDateStrings(data);

  // Add missing dates to array with undefined values
  data = addMissingDates(data);

  // Set chart size
  let width = 1000;
  let height = 500;
  let margin = {
    top: 20,
    right: 30,
    bottom: 30,
    left: 40
  };

  // Make axes scales
  let x = d3.scaleTime()
            .domain(d3.extent(data, d => d.date))
            .range([margin.left, width - margin.right]);

  let y = d3.scaleLinear()
            .domain([0, d3.max(data, d => d.value)]).nice()
            .range([height - margin.bottom, margin.top]);

  // Make axes groups
  let xAxis = g => g.attr("transform", `translate(0,${height - margin.bottom})`)
                    .call(d3.axisBottom(x)
                            .ticks(width / 80)
                            .tickSizeOuter(0));

  let yAxis = g => g.attr("transform", `translate(${margin.left},0)`)
                    .call(d3.axisLeft(y))
                    .call(g => g.select(".tick:last-of-type text").clone()
                                .attr("x", 3)
                                .attr("text-anchor", "start")
                                .attr("font-weight", "bold")
                                .text(valueLabel));

  // Make line
  let line = d3.line()
               .defined(d => !isNaN(d.value))
               .x(d => x(d.date))
               .y(d => y(d.value));

  // Make chart
  const svg = d3.select("#line-plot")
                .append("svg")
                .attr("viewBox", [0, 0, width, height]);
  
  svg.append("g")
     .call(xAxis);

  svg.append("g")
     .call(yAxis);

  svg.append("path")
     .datum(data)
     .attr("fill", "none")
     .attr("stroke", "steelblue")
     .attr("stroke-width", 1.5)
     .attr("stroke-linejoin", "round")
     .attr("stroke-linecap", "round")
     .attr("d", line);
  
  svg.selectAll("circle")
     .data(data.filter(d => !isNaN(d.value)))
     .enter()
     .append("circle")
      .attr("fill", "steelblue")
      .attr("cx", d => x(d.date))
      .attr("cy", d => y(d.value))
      .attr("r", 5);

}

/**
 * Parse input data array date strings
 */
function parseInputDateStrings(data) {
  return data.map(d => {
    p = d.date.split("-");
    return {
      date: new Date(p[0], p[1]-1, p[2]),
      value: d.value
    };
  }).sort((a, b) => d3.ascending(a.date, b.date));
}

/**
 * Add missing dates to data array with corresponding values undefined
 */
function addMissingDates(data) {
  let [minDate, maxDate] = d3.extent(data, d => d.date);

  date = new Date(minDate.getTime());
  // add additional day before min to pad the graph
  date.setDate(date.getDate() - 1);

  newData = [];
  while (date <= maxDate) {
    if (data[0].date - date === 0) {
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
