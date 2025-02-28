// xpaths have the xpaths for all the field_names
// configDetails have all the field_names which are to be morphed
const fs = require('fs');
const xml2js = require('xml2js');

// Function to apply a basic morphing method
function applyMorphing(originalValue) {
    return "MORPHED";  // Temporary
}

// morphs all the instances based on a given pathParts eg ["root", "user", "id"]. this will morph all the "id"'s which have the path /root/user/id
function morphFieldForXPath(pathParts, level, jsonData) {
    // we are at the field value
    if (level === pathParts.length)
    {
        if (Array.isArray(jsonData))
        {
            jsonData.forEach((val, ind) => {
                jsonData[ind] = applyMorphing(val);
            })
            return jsonData;
        }
        else
        {
            return applyMorphing(jsonData);
        }

    }

    if (level > pathParts.length)
        return jsonData;

    if (Array.isArray(jsonData)) {
        jsonData.forEach((val, ind) => {
            jsonData[ind][pathParts[level]] = morphFieldForXPath(pathParts, level + 1, val[pathParts[level]]);
        })
        return jsonData;
    }
    else {
        jsonData[pathParts[level]] = morphFieldForXPath(pathParts, level + 1, jsonData[pathParts[level]]);
        return jsonData;
    }
}


// Function to morph json data based on config rules
function morphData(jsonData, configDetails, xpaths) {

    // Loop through configDetails to find and morph relevant fields
    configDetails.forEach(config => {

        // Get all the XPaths for the field
        const fieldName = config.field_name;
        const paths = xpaths[fieldName] || [];

        // Morph each instance value of that field
        paths.forEach(xpath => {
            const pathParts = xpath.split('/').filter(Boolean); // Remove empty parts
            jsonData = morphFieldForXPath(pathParts, 0, jsonData);
        });

    });

    // Convert the modified JSON back to XML
    const builder = new xml2js.Builder();
    const morphedXML = builder.buildObject(jsonData);
    fs.writeFileSync('./uploads/morphed_data_files/morphed_data.xml', morphedXML);
    console.log("Morphing complete. Output saved to morphed_data.xml");
}

module.exports = {
    morphData: morphData
};





// //xml format
// <root>
//     <user>
//         <id>5</id>
//         <name>hello</name>
//     </user>
//     <user>
//         <id>6</id>
//         <name>bye</name>
//     </user>
// </root>

// // converted to jsonFormat
// {
//     root: {
//         user: [
//             {id: '5', name: 'hello'},
//             {id: '6', name: 'bye'}
//         ]
//     }
// }

// pathParts for id : ["root", "user", "id"]

/* 
    level=0      jsonData = {
                                root: {
                                    user: [
                                        {id: ['5','6'], name: 'hello'},
                                        {id: ['6','7'], name: 'bye'}
                                    ]
                                }
                            }
    
    level=1     jsonData = {
                                user: [
                                    {id: ['5','6'], name: 'hello'},
                                    {id: ['6','7'], name: 'bye'}
                                ]
                            }


    level=2     jsonData = [
                                {id: ['5','6'], name: 'hello'},
                                {id: ['6','7'], name: 'bye'}
                            ]
    

    level=3     jsonData = ['5','6']                      level=3     jsonData = '6'
    
*/