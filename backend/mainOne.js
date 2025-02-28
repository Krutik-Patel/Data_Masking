const fs = require('fs');
const xml2js = require('xml2js');
const { getXPaths, getConfigDetails } = require('./utils.js');
const morphData = require('./morph.js'); // Fixed import

async function main() {
  const configFilePath = './uploads/config_files/sample_config.xml';
  const dataFilePath = './uploads/data_files/sample_data.xml';

  // Load configuration details
  const configDetails = await getConfigDetails(configFilePath);
  console.log("Final Config Details:", configDetails);

  // Read XML data file
  const xmldata = fs.readFileSync(dataFilePath, 'utf-8');

  // Get all XPaths
  const xPaths = getXPaths(xmldata);
  console.log("All XPaths in data file:", xPaths);

  
  const parser = new xml2js.Parser({ explicitArray: false });
  let jsonData = await parser.parseStringPromise(xmldata);
  console.log("JSON representation of data file:", jsonData);

  // Morph the data
  morphData(jsonData, configDetails, xPaths); 
}

main();