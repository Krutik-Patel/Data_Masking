import fs from 'fs';
import xml2js from 'xml2js';

async function getConfigDetails(configFilePath) {
    try {
        // Read XML config file
        const xmlConfigData = fs.readFileSync(configFilePath, 'utf-8');

        // Create XML parser
        const parser = new xml2js.Parser({ explicitArray: false });

        // Parse XML file
        const result = await parser.parseStringPromise(xmlConfigData);

        // console.log(result);

        // Extract relevant details
        const configDetails = result?.fields_info?.field || [];
        // console.log(configDetails);  // Now configDetails contains an array of all field details

        return configDetails;
    } catch (err) {
        console.error("Error parsing the config XML:", err);
        return null;  // Return null in case of an error
    }
}

export default getConfigDetails;
