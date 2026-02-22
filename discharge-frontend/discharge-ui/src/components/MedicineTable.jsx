import React from "react";

export default function MedicineTable({ medicines }) {
  if (!medicines.length) return null;

  return (
    <div className="bg-white rounded-2xl shadow-lg p-6 ring-1 ring-slate-900/5 mt-6 border border-slate-100 mb-8">
      <h3 className="text-xl font-bold text-slate-800 mb-4 border-b border-slate-100 pb-2 flex items-center gap-2">
        <svg xmlns="http://www.w3.org/2000/svg" className="h-6 w-6 text-red-500" fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19.428 15.428a2 2 0 00-1.022-.547l-2.384-.477a6 6 0 00-3.86.517l-.318.158a6 6 0 01-3.86.517L6.05 15.21a2 2 0 00-1.806.547M8 4h8l-1 1v5.172a2 2 0 00.586 1.414l5 5c1.26 1.26.367 3.414-1.415 3.414H4.828c-1.782 0-2.674-2.154-1.414-3.414l5-5A2 2 0 009 10.172V5L8 4z" />
        </svg>
        Prescribed Medicines
      </h3>
      <div className="overflow-x-auto rounded-lg border border-slate-100">
        <table className="w-full text-left border-collapse bg-white">
          <thead>
            <tr className="bg-slate-50 border-b border-slate-200">
              <th className="px-5 py-3 text-xs font-bold text-slate-500 uppercase tracking-wider">Medicine Name / Dosage</th>
            </tr>
          </thead>
          <tbody className="divide-y divide-slate-100">
            {medicines.map((m, i) => (
              <tr key={i} className="hover:bg-slate-50/50 transition-colors">
                <td className="px-5 py-3 text-slate-700 text-sm font-medium flex items-center gap-2">
                  <span className="w-2 h-2 rounded-full bg-blue-400"></span>
                  {m}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}