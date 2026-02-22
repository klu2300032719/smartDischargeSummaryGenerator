import React, { useState } from "react";
import { generateSummary } from "../api/api";

export default function UploadForm({
  setSummary,
  setMedicines,
  setPatient,
  setLanguage
}) {
  const [file, setFile] = useState(null);
  const [language, setLangLocal] = useState("en");
  const [patientName, setPatientName] = useState("");

  const handleSubmit = async () => {
    if (!file) return alert("Upload file");

    const formData = new FormData();
    formData.append("patientName", patientName);
    formData.append("file", file);
    formData.append("language", language);

    try {
      const res = await generateSummary(formData);

      setSummary(res.data);
      setPatient(patientName);

      // simple medicine extraction
      const meds = res.data
  .split("\n")
  .filter((line) => /^\s*\d+\./.test(line))
  .map((line) => line.replace(/^\s*\d+\.\s*/, ""));

      setMedicines(meds);
    } catch (err) {
      alert("Error generating summary");
    }
  };

  return (
    <div className="flex flex-col gap-6">
      {/* Patient Name */}
      <input
        placeholder="Patient Name"
        className="px-4 py-3 rounded-xl border"
        onChange={(e) => setPatientName(e.target.value)}
      />

      {/* File Upload */}
      <input type="file" onChange={(e) => setFile(e.target.files[0])} />

      {/* Language Dropdown */}
      <select
        className="px-4 py-3 rounded-xl border"
        value={language}
        onChange={(e) => {
          setLangLocal(e.target.value);
          setLanguage(e.target.value); // send to Home for voice playback
        }}
      >
        <option value="en">English</option>
        <option value="hi">Hindi</option>
        <option value="te">Telugu</option>
        <option value="ta">Tamil</option>
        <option value="kn">Kannada</option>
        <option value="ml">Malayalam</option>
        <option value="mr">Marathi</option>
        <option value="bn">Bengali</option>
        <option value="gu">Gujarati</option>
        <option value="pa">Punjabi</option>
        <option value="ur">Urdu</option>
      </select>

      {/* Generate Button */}
      <button
        className="bg-indigo-600 text-white py-3 rounded-xl hover:bg-indigo-700"
        onClick={handleSubmit}
      >
        Generate Summary
      </button>
    </div>
  );
}