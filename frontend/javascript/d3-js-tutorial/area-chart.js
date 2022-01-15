const canvas = d3.select(".canvas");
const revenueData = [
  52.13, 53.98, 67.0, 89.7, 99.0, 130.28, 166.7, 234.98, 345.44, 443.34, 543.7,
  556.13,
];

const months = [
  "January",
  "February",
  "March",
  "April",
  "May",
  "June",
  "July",
  "August",
  "September",
  "October",
  "November",
  "December",
];

// Add SVG element
const svgWidth = 600;
const svgHeight = 600;
const svg = canvas
  .append("svg")
  .attr("width", svgWidth)
  .attr("height", svgHeight);
const margin = { top: 20, bottom: 20, left: 40, right: 40 };
const plotHeight = svgHeight - margin.top - margin.bottom;
const plotWidth = svgWidth - margin.left - margin.right;
const mainCanvas = svg
  .append("g")
  .attr("width", plotWidth)
  .attr("height", plotHeight)
  .attr("transform", `translate(${margin.left},${margin.top})`);

const parseMonths = d3.timeParse("%B");

const scaleX = d3
  .scaleTime()
  .domain(d3.extent(months, (d) => parseMonths(d)))
  .range([0, plotWidth]);

const scaleY = d3
  .scaleLinear()
  .domain([0, d3.max(revenueData, (d) => d)])
  .range([plotHeight, 0]);

const areaChart = d3
  .area()
  .x((_d, i) => scaleX(parseMonths(months[i])))
  .y0(plotHeight)
  .y1((d, _i) => scaleY(d));

const valueLine = d3
  .line()
  .x((_d, i) => scaleX(parseMonths(months[i])))
  .y((d, _i) => scaleY(d));

mainCanvas
  .append("path")
  .attr("fill", "orange")
  .attr("class", "area")
  .attr("d", areaChart(revenueData));

mainCanvas
  .append("path")
  .data([revenueData])
  .attr("class", "line")
  .attr("d", valueLine);

// Add circles to data points
mainCanvas
  .selectAll("circle")
  .data(revenueData)
  .enter()
  .append("circle")
  .attr("class", "circle")
  .attr("cx", (_d, i) => scaleX(parseMonths(months[i])))
  .attr("cy", (d, _i) => scaleY(d))
  .attr("r", 5);

// Add axis
const xAxis = d3.axisBottom(scaleX).tickFormat(d3.timeFormat("%b"));
const yAxis = d3.axisLeft(scaleY).ticks(4).tickPadding(5).tickSize(5);
mainCanvas
  .append("g")
  .attr("transform", `translate(0,${plotHeight})`)
  .call(xAxis);
mainCanvas.append("g").call(yAxis);
