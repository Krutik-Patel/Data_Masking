import fs from 'fs';
import xml2js from 'xml2js';
import { getXPaths, getConfigDetails } from './utils.js';
import morphData from './morph.js';

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

    // Convert XML to JSON
    const parser = new xml2js.Parser({ explicitArray: false });
    let jsonData = await parser.parseStringPromise(xmldata);
    console.log("JSON representation of data file:", jsonData);

    // Morph the data
    morphData(jsonData, configDetails, xPaths);
}

main();
