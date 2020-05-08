function drawGraph(dates, values) {

  let width = 500;
  let height = 250;

  let svg = d3.select("#d3-visualization")
              .append("svg")
              .attr("viewBox", "0 0 " + width + " " + height);
  
  let data = [];

  for (let i = 0; i < dates.length; i++) {
    datePieces = dates[i].split("-");
    data.push({
      date: new Date(datePieces[0], datePieces[1], datePieces[2]),
      value: values[i]
    })
  }
  console.log(data);

  let padding = 50;

  let minDate = d3.min(data, function(d) {return d.date;});

  let maxDate = d3.max(data, function(d) {return d.date;});

  let xScale = d3.scaleTime()
                 .domain([minDate, maxDate])
                 .range([padding, width - padding]);
    
  let yScale = d3.scaleLinear()
                       .domain([0, d3.max(data, function(d) {return d.value;})])
                       .range([height - padding, padding]);

  let format = d3.timeFormat("%b %d");
  let xAxis = d3.axisBottom(xScale)
                .tickFormat(format);
                  
  svg.append("g")
     .attr("class", "axis x-axis")
     .attr("transform", "translate(0," + (height - padding) + ")")
     .call(xAxis);

  let yAxis = d3.axisLeft(yScale);

  svg.append("g")
     .attr("class", "axis y-axis")
     .attr("transform", "translate(" + padding + ",0)")
     .call(yAxis);

  let line = d3.line()
               .x(function(d) {return xScale(d.date);})
               .y(function(d) {return yScale(d.value);});
  
  svg.append("svg:path")
     .attr("d", line(data))
     .attr("stroke-width", 1.5)
     .attr("stroke", "#000000")
     .attr("fill", "none");

  svg.selectAll("circle")
      .data(data)
      .enter()
      .append("circle")
      .attr("class", "data-point")
      .attr("cx", function(d) {
        return xScale(d.date);
      })
      .attr("cy", function(d) {
        return yScale(d.value);
      })
      .attr("r", 2.5);

}