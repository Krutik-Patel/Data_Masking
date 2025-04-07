import React, { useState } from "react";
import axios from "axios";

function Upload() {
    const [file, setFile] = useState(null);
    const [configMessage, setConfigMessage] = useState("");
    const [dataMessage, setDataMessage] = useState("");
    const [configRules, setConfigRules] = useState([]);
    const [maskedData, setMaskedData] = useState("");
    const [maskedDataMessage, setMaskedDataMessage] = useState("");
 
    const handleFileChange = (event) => {
        setFile(event.target.files[0]);
    };

    const handleUpload = async (upload_path) => {
        if (!file) return;

        const formData = new FormData();
        formData.append("file", file);

        try {
            const response = await axios.post(`http://localhost:8080/uploads/${upload_path}`, formData, {
                headers: { "Content-Type": "multipart/form-data" }
            });

            
            if (upload_path === 'config') {
                setConfigMessage(response.data.message);
                setConfigRules(response.data.additionalText);
                console.log(response.data.additionalText);
            } else if (upload_path === 'data') {
                setDataMessage(response.data.message);
                console.log(response.data.additionalText);
            }
        } catch (error) {
            if (upload_path === 'config') {
                setConfigMessage("Upload failed");
                console.log("thisb");
            } else if (upload_path === 'data') {
                setDataMessage("Upload failed");
                console.log("thisc");
            }
        }
    };
    
    const maskData = async () => {
        try {
            const response = await axios.get("http://localhost:8080/maskData");
            setMaskedDataMessage(response.data.message);
            setMaskedData(response.data.additionalText);;
            console.log(response.data.additionalText);
        } catch (error) {
            console.error("Error masking data:", error);
        }
    };

    return (
        <div style={{ textAlign: "center", padding: "20px" }}>
            <h2>Config File Upload</h2>
            <input type="file" onChange={handleFileChange} />
            <button onClick={() => handleUpload('config')}>Upload</button>
            <p>{configMessage}</p>
            <h2>Data file Upload</h2>
            <input type="file" onChange={handleFileChange} />
            <button onClick={() => handleUpload('data')}>Upload</button>
            <p>{dataMessage}</p>
            {dataMessage.length > 0 && (
                <div>
                    <button onClick={() => maskData()}>MASK DATA</button>
                </div>
            )}
            {configRules.length > 0 && typeof configRules === "string" && (
                <div>
                    <h3>Config Rules (XML):</h3>
                    <pre>{configRules}</pre>
                </div>
            )}
            {maskedData.length > 0 && (
                <div>
                    <h3>Masked Data: {maskedDataMessage}</h3>
                    <pre>{maskedData}</pre>
                </div>
            )}
            {maskedData.length > 0 && (
                <div>
                    <h3>Masked Data:</h3>
                    <pre>{maskedData}</pre>
                </div>
            )}
        </div>
    );
}


export default Upload;