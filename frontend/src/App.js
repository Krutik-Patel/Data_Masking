import React, { useState } from "react";
import axios from "axios";

function Upload() {
    const [file, setFile] = useState(null);
    const [configMessage, setConfigMessage] = useState("");
    const [dataMessage, setDataMessage] = useState("");
    const [configRules, setConfigRules] = useState([]);
    const [maskedData, setMaskedData] = useState("");
 
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
                setConfigRules(response.data.configRules);
                console.log(response.data.configRules);
            } else if (upload_path === 'data') {
                setDataMessage(response.data.message);
                console.log("thisa");
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
            setMaskedData(response.data.maskedData);
            console.log(response.data.maskedData);
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
            {configRules.length > 0 && (
                <div>
                    <h3>Config Rules:</h3>
                    <table border="1" style={{ margin: "0 auto" }}>
                        <thead>
                            <tr>
                                <th>Field Name</th>
                                {/* <th>Field XPath</th> */}
                                <th>Data Type</th>
                                <th>Is Primary Key</th>
                                <th>Morphing Methods</th>
                            </tr>
                        </thead>
                        <tbody>
                            {configRules.map((rule, index) => (
                                <tr key={index}>
                                    <td>{rule.field_name}</td>
                                    {/* <td>{rule.field_xPath}</td> */}
                                    <td>{rule.dataType}</td>
                                    <td>{rule.isPrimaryKey}</td>
                                    <td>
                                        {rule.morphing_methods && rule.morphing_methods.morphing_method ? (
                                            <ul>
                                                {Object.entries(rule.morphing_methods.morphing_method).map(([key, value], idx) => (
                                                    <li key={idx}>{`${key}: ${value}`}</li>
                                                ))}
                                            </ul>
                                        ) : (
                                            "N/A"
                                        )}
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
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