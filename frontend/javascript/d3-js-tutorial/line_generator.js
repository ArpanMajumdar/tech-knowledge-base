const data = [
  { x: 10, y: 10 },
  { x: 15, y: 20 },
  { x: 20, y: 40 },
  { x: 25, y: 7 },
  { x: 30, y: 10 },
];

const canvas = d3.select(".canvas");
const svgWidth = 600;
const svgHeight = 600;
const svg = canvas
  .append("svg")
  .attr("width", svgWidth)
  .attr("height", svgHeight);

const margin = { top: 20, bottom: 20, left: 20, right: 20 };
const plotWidth = svgWidth - margin.left - margin.right;
const plotHeight = svgHeight - margin.top - margin.bottom;

const area = svg
  .append("g")
  .attr("width", plotWidth)
  .attr("height", plotHeight)
  .attr("transform", `translate(${margin.left},${margin.top})`);

const lineGen = d3
  .line()
  .x((d, i) => d["x"] * 20)
  .y((d, i) => d["y"] * 10)
  .curve(d3.curveCardinal);

area
  .append("path")
  .attr("stroke", "green")
  .attr("stroke-width", 3)
  .attr("fill", "none")
  .attr("d", lineGen(data));
