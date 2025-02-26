const express = require("express");
const multer = require("multer");
const cors = require("cors");
const path = require("path");

const app = express();
app.use(cors());

// Set up file storage
const storage = multer.diskStorage({
    destination: "./uploads/",
    filename: (req, file, cb) => {
        cb(null, file.originalname);
    }
});
const upload = multer({ storage });

// Handle file upload requests
app.post("/upload", upload.single("file"), (req, res) => {
    res.json({ message: "File uploaded successfully", filename: req.file.filename });
});

app.use("/uploads", express.static(path.join(__dirname, "uploads")));

app.listen(5000, () => console.log("Server running on port 5000"));


/* TO DO :
    - Take the config file from the user and store it in uploads/config_files
    - Take the data json file from the user and store it in uploads/data_files
    
    - Using the data_file extract xpaths for each field given in the config_file.
    - morph the fields as per the morphing_methods given in the config_file
    - For the realted keys make sure the same morphing is applied.
*/
