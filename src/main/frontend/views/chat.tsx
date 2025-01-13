import React, { useState } from 'react';
import { Button, TextField } from "@vaadin/react-components";
import * as pdfjsLib from 'pdfjs-dist';
import { ChatAiService } from "Frontend/generated/endpoints";

export default function Chat() {
    const [question, setQuestion] = useState<string>("");
    const [response, setResponse] = useState<string>("");
    const [pdfText, setPdfText] = useState<string>("");

    // Fonction pour lire le PDF et extraire le texte
    const handleFileChange = async (event: React.ChangeEvent<HTMLInputElement>) => {
        const file = event.target.files?.[0];
        if (file && file.type === 'application/pdf') {
            const text = await extractTextFromPDF(file);
            setPdfText(text);  // Affiche le texte extrait
        }
    };

    // Fonction pour extraire le texte d'un PDF
    const extractTextFromPDF = async (file: File) => {
        const fileReader = new FileReader();
        return new Promise<string>((resolve, reject) => {
            fileReader.onload = async () => {
                // @ts-ignore
                const typedArray = new Uint8Array(fileReader.result);
                const pdf = await pdfjsLib.getDocument(typedArray).promise;
                let text = "";
                for (let i = 0; i < pdf.numPages; i++) {
                    const page = await pdf.getPage(i + 1);
                    const content = await page.getTextContent();
                    content.items.forEach((item: any) => {
                        text += item.str + " ";
                    });
                }
                resolve(text);
            };
            fileReader.onerror = reject;
            fileReader.readAsArrayBuffer(file);
        });
    };

    async function send() {
        try {
            let response: string;
            [response] = await Promise.all([ChatAiService.ragChat(question, pdfText)]);
            setResponse(response);
        } catch (error) {
            console.error("Error fetching response:", error);
        }
    }

    return (
        <div style={styles.container}>
            <h3 style={styles.header}>Chat Bot</h3>
            <div style={styles.card}>
                <TextField
                    style={styles.textField}
                    value={question}
                    onChange={(e) => setQuestion(e.target.value)}
                    placeholder="Type your question..."
                />
                <input type="file" accept="application/pdf" onChange={handleFileChange} />
                <Button
                    theme="primary"
                    onClick={send}
                    style={styles.button}
                >
                    Send
                </Button>
            </div>

            {response && (
                <div style={styles.responseContainer}>
                    <strong style={styles.responseHeader}>Response:</strong>
                    <p style={styles.responseText}>{response}</p>
                </div>
            )}
        </div>
    );
}

const styles = {
    container: {
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        padding: '40px',
        backgroundColor: '#f9fafb',
        minHeight: '100vh',
    },
    header: {
        fontSize: '36px',
        fontWeight: '600',
        color: '#4c72b0',
        marginBottom: '20px',
    },
    card: {
        backgroundColor: '#ffffff',
        padding: '20px',
        borderRadius: '10px',
        boxShadow: '0px 4px 8px rgba(0, 0, 0, 0.1)',
        width: '100%',
        maxWidth: '500px',
        marginBottom: '20px',
    },
    textField: {
        width: '100%',
        padding: '12px',
        marginBottom: '15px',
        border: '1px solid #ddd',
        borderRadius: '8px',
        fontSize: '16px',
        outline: 'none',
        transition: 'border-color 0.3s',
    },
    button: {
        width: '100%',
        padding: '15px',
        backgroundColor: '#4c72b0',
        color: 'white',
        border: 'none',
        borderRadius: '8px',
        fontSize: '16px',
        cursor: 'pointer',
        transition: 'background-color 0.3s',
    },
    responseContainer: {
        backgroundColor: '#e0f7fa',
        borderLeft: '6px solid #00796b',
        padding: '15px',
        borderRadius: '8px',
        maxWidth: '500px',
        marginTop: '20px',
    },
    responseHeader: {
        fontSize: '18px',
        fontWeight: '600',
        color: '#00796b',
    },
    responseText: {
        fontSize: '16px',
        color: '#00796b',
        marginTop: '8px',
    },
};



