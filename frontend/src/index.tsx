import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import App from './App';
import reportWebVitals from './reportWebVitals';
import { BrowserRouter } from 'react-router-dom';
import {ChakraProvider, ColorModeScript, extendTheme, type ThemeConfig} from "@chakra-ui/react";
import type {GlobalStyleProps} from "@chakra-ui/theme-tools"

const config: ThemeConfig = {
        initialColorMode: "dark",
        useSystemColorMode: false
}

const theme = extendTheme({
    config,
    styles: {
        global: (props:GlobalStyleProps)=> ({
            'html, body': {
                color: props.colorMode === "dark" ? "white" : 'dark.900',
                backgroundColor: props.colorMode === "dark" ? "dark.700" : "aliceblue"
            }
        })
    },
    colors:{
        dark: {
            50: '#d5d7e0',
            100: '#acaebf',
            200: '#8c8fa3',
            300: '#666980',
            400: '#4d4f66',
            500: '#34354a',
            600: '#2b2c3d',
            700: '#1d1e30',
            800: '#0c0d21',
            900: '#01010a'
        },
        coolgray: {
            50: "#8388AF",
            100:"#767CA7",
            200:"#6A70A0",
            300:"#5F6695",
            400:"#585E89",
            500:"#50557C",
            600:"#484C70",
            700:"#404364",
            800:"#383A57",
            900:"#30324B",
        }
    }
})


const root = ReactDOM.createRoot(
  document.getElementById('root') as HTMLElement
);
root.render(
   <ChakraProvider theme={theme}>
        <React.StrictMode>
            <BrowserRouter>
                <ColorModeScript initialColorMode={theme.config.initialColorMode} />
                <App />
            </BrowserRouter>
        </React.StrictMode>
   </ChakraProvider>


);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
