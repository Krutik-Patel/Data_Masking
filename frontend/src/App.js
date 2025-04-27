import React, { useState } from "react";
import axios from "axios";
import { ToastContainer, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import "./App.css";
import ConfigUploader from "./components/ConfigUploader";
import DataUploader from "./components/DataUploader";
import { useEffect } from "react";


// const BASE_URL = "https://980f-119-161-98-68.ngrok-free.app";
// const BASE_URL = "https://907f-103-156-19-229.ngrok-free.app/";
const BASE_URL = "http://localhost:8080";

function Upload() {
    const [configMessage, setConfigMessage] = useState("");
    const [dataMessage, setDataMessage] = useState("");
    const [dataText, setDataText] = useState("");
    const [configRules, setConfigRules] = useState("");
    const [maskedData, setMaskedData] = useState("");
    const [maskedDataMessage, setMaskedDataMessage] = useState("");
    const [loading, setLoading] = useState(false);
    const [progress, setProgress] = useState(0);
    const [darkMode, setDarkMode] = useState(true);
    const toggleDarkMode = () => setDarkMode((prev) => !prev);
    useEffect(() => {
        document.body.classList.remove("light-mode", "dark-mode");
        document.body.classList.add(darkMode ? "dark-mode" : "light-mode");
    }, [darkMode]);

    const configUpload = async (file) => {
        if (!file) return;
        setLoading(true);
        setProgress(20);
        const formData = new FormData();
        formData.append("file", file);

        try {
            const response = await axios.post(`${BASE_URL}/uploads/config`, formData, {
                headers: { "Content-Type": "multipart/form-data" },
                onUploadProgress: (progressEvent) => {
                    const percent = Math.round((progressEvent.loaded * 100) / progressEvent.total);
                    setProgress(percent);
                }
            });
            setConfigMessage(response.data.message);
            setConfigRules(response.data.additionalText);
            toast.success("Config uploaded successfully");

        } catch (error) {
            const errMsg = error.response?.data?.message || "Upload failed";
            toast.error(errMsg);
            setConfigMessage(errMsg);
        } finally {
            setLoading(false);
            setProgress(0);
        }
    }

    const dataUpload = async (file) => {
        if (!file) return;
        setLoading(true);
        setProgress(20);
        const formData = new FormData();
        formData.append("file", file);

        try {
            const response = await axios.post(`${BASE_URL}/uploads/data`, formData, {
                headers: { "Content-Type": "multipart/form-data" },
                onUploadProgress: (progressEvent) => {
                    const percent = Math.round((progressEvent.loaded * 100) / progressEvent.total);
                    setProgress(percent);
                }
            });


            setDataMessage(response.data.message);
            setDataText(response.data.additionalText);
            toast.success("Data uploaded successfully");
        } catch (error) {
            const errMsg = error.response?.data?.message || "Upload failed";
            toast.error(errMsg);
            setDataMessage(errMsg);
        } finally {
            setLoading(false);
            setProgress(0);
        }
    };

    const maskData = async () => {
        setLoading(true);
        setProgress(10);
        try {
            const response = await axios.get(`${BASE_URL}/maskData`);
            setMaskedDataMessage(response.data.message);
            setMaskedData(response.data.additionalText);
            toast.success("Data masked successfully");
        } catch (error) {
            toast.error("Error masking data");
            console.error("Error masking data:", error);
        } finally {
            setLoading(false);
            setProgress(0);
        }
    };

    return (
        <div className={`upload-container ${darkMode ? "dark-mode" : "light-mode"}`}>
            <ToastContainer position="top-right" autoClose={3000} hideProgressBar={false} newestOnTop />
            <h1>Data Masking App</h1>
            <button onClick={toggleDarkMode} className="toggle-mode-btn">
                Switch to {darkMode ? "Light" : "Dark"} Mode
            </button>
            {progress > 0 && (
                <div className="progress-bar">
                    <div className="progress-fill" style={{ width: `${progress}%` }}></div>
                </div>
            )}

            <ConfigUploader
                onUpload={(file) => configUpload(file)}
                message={configMessage}
                rules={configRules}
                loading={loading}
                darkMode={darkMode}
            />
            <div className="card">
                <DataUploader
                    onUpload={(file) => dataUpload(file)}
                    message={dataMessage}
                    onMask={maskData}
                    loading={loading}
                    showData={dataText}
                    maskedData={maskedData}
                    maskedDataMessage={maskedDataMessage}
                    darkMode={darkMode}
                />
            </div>
        </div>
    );
}

export default Upload;
