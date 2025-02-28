import React, { useState } from "react";
import axios from "axios";

function Upload() {
    const [file, setFile] = useState(null);
    const [message, setMessage] = useState("");

    const handleFileChange = (event) => {
        setFile(event.target.files[0]);
    };

    const handleUpload = async (upload_path) => {
        if (!file) return;

        const formData = new FormData();
        formData.append("file", file);

        try {
            const response = await axios.post(`http://localhost:5000/uploads/${upload_path}`, formData, {
                headers: { "Content-Type": "multipart/form-data" }
            });
            setMessage(response.data.message);

        } catch (error) {
            setMessage("Upload failed");
        }
    };

    // const showConfig = async () => {
    //     try {
    //         const response = await axios.get('http://localhost:5000/uploads/config_files/config.xml', {
    //             headers: { 'Content-Type': 'application/xml' }
    //         });
    //         const parser = new DOMParser();
    //         const xmlDoc = parser.parseFromString(response.data, 'application/xml');
    //         const configData = get
    //     }
    // }
    
    return (
        <div style={{ textAlign: "center", padding: "20px" }}>
            <h2>Config File Upload</h2>
            <input type="file" onChange={handleFileChange} />
            <button onClick={() => handleUpload('config')}>Upload</button>
            <p>{message}</p>
            <h2>Data file Upload</h2>
            <input type="file" onChange={handleFileChange} />
            <button onClick={() => handleUpload('data')}>Upload</button>
            <p>{message}</p>
        </div>
    );
}


export default Upload;