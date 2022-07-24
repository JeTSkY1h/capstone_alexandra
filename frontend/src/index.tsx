import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import App from './App';
import reportWebVitals from './reportWebVitals';
import { BrowserRouter } from 'react-router-dom';
import {MantineProvider} from "@mantine/core";

const root = ReactDOM.createRoot(
  document.getElementById('root') as HTMLElement
);
root.render(
    <MantineProvider theme={{
            colors: {
                "cool-gray": [
                    "#8388AF",
                    "#767CA7",
                    "#6A70A0",
                    "#5F6695",
                    "#585E89",
                    "#50557C",
                    "#484C70",
                    "#404364",
                    "#383A57",
                    "#30324B",

                ],
                dark: [
                    '#d5d7e0',
                    '#acaebf',
                    '#8c8fa3',
                    '#666980',
                    '#4d4f66',
                    '#34354a',
                    '#2b2c3d',
                    '#1d1e30',
                    '#0c0d21',
                    '#01010a',
                ],
            },
            colorScheme: 'dark',
            primaryColor: "cool-gray",
            primaryShade: 7
    }} withGlobalStyles withNormalizeCSS>
        <React.StrictMode>
            <BrowserRouter>
                <App />
            </BrowserRouter>
        </React.StrictMode>
     </MantineProvider>

);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
