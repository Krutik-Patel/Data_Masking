import fs from 'fs';
import xml2js from 'xml2js';

import getXPaths from "./test.js";   // for getting the xpaths

import getConfigDetails from './configDetails.js';  // for getting the config details

import morphData from './morph.js';  // for morphing the data file



async function main() {

    // getting the config details
    const configFilePath = './uploads/config_files/sample_config.xml';
    const configDetails = await getConfigDetails(configFilePath);
    console.log("Final Config Details:", configDetails);


    // getting the xpaths for all fields in the data file
    const dataFilePath = './uploads/data_files/sample_data.xml'
    const xmldata = fs.readFileSync(dataFilePath, 'utf-8');
    const xPaths = getXPaths(xmldata);
    console.log("All xPaths in data file", xPaths);


    // getting the datafile converted to json
    const parser = new xml2js.Parser({explicitArray: false});
    const builder = new xml2js.Builder();
    let jsonData = await parser.parseStringPromise(xmldata);
    console.log("json of the datafile:" ,jsonData);

    // morph the data file
    morphData(jsonData, configDetails, xPaths);

}


main();
// things needed: the xpaths, configDetails, original data file
