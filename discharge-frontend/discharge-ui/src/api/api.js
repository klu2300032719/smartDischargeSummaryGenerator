import axios from "axios";

const BASE = "http://localhost:2819";

export const generateSummary = (formData) =>
  axios.post(`${BASE}/generate`, formData);

export const downloadPdf = (name) =>
  axios.get(`${BASE}/download/${name}`, { responseType: "blob" });

// ⭐ Chatbot API
export const askChatbot = (question) =>
  axios.post(`${BASE}/chat`, { question });