import React, { useState } from "react";
import UploadForm from "../components/UploadForm";
import SummaryView from "../components/SummaryView";
import MedicineTable from "../components/MedicineTable";
import Chatbot from "../components/Chatbot";

export default function Home() {
  const [summary, setSummary] = useState("");
  const [medicines, setMedicines] = useState([]);
  const [patient, setPatient] = useState("");
  const [language, setLanguage] = useState("en"); // ⭐ added for voice

  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100 font-sans text-slate-800 pb-12">
      {/* Navbar */}
      <nav className="bg-white/90 backdrop-blur-md sticky top-0 z-50 shadow-sm border-b border-blue-100">
        <div className="container mx-auto px-4 md:px-6 h-20 flex items-center justify-between">
          <div className="flex items-center gap-3">
            <div className="bg-indigo-600 p-2 rounded-lg text-white">
              <svg xmlns="http://www.w3.org/2000/svg" className="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2}>
                <path strokeLinecap="round" strokeLinejoin="round" d="M19.428 15.428a2 2 0 00-1.022-.547l-2.384-.477a6 6 0 00-3.86.517l-.318.158a6 6 0 01-3.86.517L6.05 15.21a2 2 0 00-1.806.547M8 4h8l-1 1v5.172a2 2 0 00.586 1.414l5 5c1.26 1.26.367 3.414-1.415 3.414H4.828c-1.782 0-2.674-2.154-1.414-3.414l5-5A2 2 0 009 10.172V5L8 4z" />
              </svg>
            </div>
            <h1 className="text-xl md:text-2xl font-bold bg-clip-text text-transparent bg-gradient-to-r from-blue-700 to-indigo-700">
              Smart Discharge Summary
            </h1>
          </div>
        </div>
      </nav>

      <main className="container mx-auto px-4 md:px-6 py-8 grid gap-8">
        {/* About Section */}
        <section className="bg-white rounded-2xl shadow-sm border border-blue-50 p-6 md:p-8">
          <h2 className="text-xl font-bold mb-3 text-indigo-900">About Us</h2>
          <p className="text-slate-600 leading-relaxed text-sm md:text-base max-w-3xl">
            Smart Discharge Summary Generator automates summaries, improves patient understanding,
            and enhances hospital workflow using AI + multilingual accessibility.
          </p>
        </section>

        <div className="grid lg:grid-cols-3 gap-8 items-start">
          <div className="lg:col-span-2 space-y-8">
            {/* Upload */}
            <div className="bg-white rounded-2xl shadow-xl p-6 md:p-8">
              <h2 className="text-lg font-bold text-slate-800 mb-6">
                Upload & Generate
              </h2>

              <UploadForm
                setSummary={setSummary}
                setMedicines={setMedicines}
                setPatient={setPatient}
                setLanguage={setLanguage}
              />
            </div>

            {/* Summary + Medicines */}
            {(summary || medicines.length > 0) && (
              <div className="space-y-8">
                <SummaryView
                  summary={summary}
                  patient={patient}
                  language={language}  // ⭐ needed for voice
                />
                <MedicineTable medicines={medicines} />
              </div>
            )}
          </div>

          {/* Chatbot */}
          <div className="lg:col-span-1">
            <div className="sticky top-24 bg-white rounded-2xl shadow-xl overflow-hidden h-[600px] flex flex-col">
              <Chatbot />
            </div>
          </div>
        </div>
      </main>
    </div>
  );
}