const express = require("express");
const multer = require("multer");
const cors = require("cors");
const path = require("path");
const { getXPaths, getConfigDetails } = require("./utils");
const fs = require("fs");
const app = express();
const { main } = require('./mainOne');
app.use(cors());

// Configure multer for config files
const configStorage = multer.diskStorage({
    destination: "./uploads/config_files/",
    filename: (req, file, cb) => {
        cb(null, file.originalname);
    }
});
const uploadConfig = multer({ storage: configStorage });

// Configure multer for data files
const dataStorage = multer.diskStorage({
    destination: "./uploads/data_files/",
    filename: (req, file, cb) => {
        cb(null, file.originalname);
    }
});
const uploadData = multer({ storage: dataStorage });

// Handle config file upload
app.post("/uploads/config", uploadConfig.single("file"), async (req, res) => {
    console.log("Received a request for /uploads/config");
    const filePath = path.join(__dirname, "uploads/config_files", req.file.filename);
    // const xmlData = fs.readFileSync(filePath, 'utf8');
    const rules = await getConfigDetails(filePath);
    res.json({ 
        message: "Config file uploaded successfully", 
        filename: req.file.filename,
        configRules: rules
    });
});

// Handle data file upload
app.post("/uploads/data", uploadData.single("file"), (req, res) => {
    console.log("Received a request for /uploads/data");
    res.json({ message: "Data file uploaded successfully", filename: req.file.filename });
});

app.get("/maskData", async (req, res) => {
    console.log("Received a request for /maskData");
    main();
    const dataFilePath = './uploads/morphed_data_files/morphed_data.xml';
    const xmldata = fs.readFileSync(dataFilePath, 'utf-8');
    res.json({ message: "Data masked successfully", maskedData: xmldata });
});

// Serve uploaded files statically
app.use("/uploads", express.static(path.join(__dirname, "uploads")));

app.listen(5000, () => console.log("Server running on port 5000"));


/* TO DO :
    - Take the config file from the user and store it in uploads/config_files
    - Take the xml data file from the user and store it in uploads/data_files
    
    - Using the data_file extract xpaths for each field given in the config_file.
    - morph the fields as per the morphing_methods given in the config_file
    - For the realted keys make sure the same morphing is applied.
*/


/* IDEAS FOR EXTENSION

    currently in the config file only the inner most fields info is being provided.
    What we can do is take the field info for other fields too and then make a heirarchical morph decision that if a method 'M' is used on a field 'F' then method 'M' will also be applied to all the children fields of the field 'F'.

*/