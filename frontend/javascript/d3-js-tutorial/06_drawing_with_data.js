const dataset = [25, 7, 5, 26, 11, 46, 24, 32, 54];

d3.select(".canvas")
  .selectAll("div")
  .data(dataset)
  .enter()
  .append("div")
  .attr("class", "bar")
  .style("height", (d) => `${d * 5}px`);

const svgWidth = 1200;
const svgHeight = 300;
const svg = d3
  .select("body")
  .append("svg")
  .attr("width", svgWidth)
  .attr("height", svgHeight);

svg
  .selectAll("circle")
  .data(dataset)
  .enter()
  .append("circle")
  .attr("cx", (d, i) => i * 100 + 50)
  .attr("cy", svgHeight / 2)
  .attr("r", (d) => d)
  .attr("fill", "yellow")
  .attr("stroke", "orange")
  .attr("stroke-width", "5px");

const svg2Width = 500;
const svg2Height = 300;
const svg2 = d3
  .select("body")
  .append("svg")
  .attr("width", svg2Width)
  .attr("height", svg2Height);

const barWidth = svg2Width / dataset.length;
const barPadding = 2;

svg2
  .selectAll("rect")
  .data(dataset)
  .enter()
  .append("rect")
  .attr("x", (d, i) => i * barWidth)
  .attr("y", (d, i) => svg2Height - d * 5)
  .attr("width", barWidth - barPadding)
  .attr("height", (d, i) => d * 5)
  .style("fill", (d, i) => `rgb(0,50,${Math.round(d * 5)})`);

svg2
  .selectAll("text")
  .data(dataset)
  .enter()
  .append("text")
  .text((d) => d)
  .attr("x", (d, i) => i * barWidth + (barWidth - barPadding) / 2)
  .attr("y", (d, i) => svg2Height - d * 5 + 15)
  .attr("font-family", "sans-serif")
  .attr("font-size", "12px")
  .attr("fill", "white")
  .attr("text-anchor", "middle");

dataset2D = [
  [5, 20],
  [480, 90],
  [250, 50],
  [100, 33],
  [330, 95],
  [410, 12],
  [475, 44],
  [25, 67],
  [85, 21],
  [220, 88],
];

const svg3Width = 600;
const svgHeight = 600;
const svg = d3
  .select("body")
  .append("svg")
  .attr("width", svg3Width)
  .attr("height", svgHeight);

svg
  .selectAll("circle")
  .data(dataset2D)
  .enter()
  .append("circle")
  .attr("cx", (d) => d[0] + 50)
  .attr("cy", (d) => svgHeight - d[1] - 300)
  .attr("r", (d) => Math.sqrt(svgHeight - d[1]))
  .attr("fill", "red");

svg
  .selectAll("text")
  .data(dataset2D)
  .enter()
  .append("text")
  .text((d) => `(${d[0]},${d[1]})`)
  .attr("x", (d) => d[0] + 30)
  .attr("y", (d) => svgHeight - d[1] - 300)
  .attr("font-size", "10px");
