import { useEffect, useId, useState } from "react";
import "./Input.css"

interface InputProps {
    Value: string;
    setValue: Function;
    label: string;
    type?: string;
    autofocus?: boolean;
}

export default function Input(props: InputProps){

    const [active, setActive] = useState(false);
    const id = useId();


    const type = props.type? props.type: "text";

    useEffect(()=>{
        if(props.Value.length <= 0) {
            setActive(false);
        } else {
            setActive(true);
        }
    }, [props.Value])

    const handleBlur = ()=> {
        if(props.Value.length <=0 ) {
            setActive(false)
        } else {
            setActive(true)
        }
    }

    return (
        <div className="my-input-wrapper">
            <label className={active ? "active" : ""} htmlFor={id}>{props.label}</label>

            {props.autofocus ?
                <input
                    autoFocus
                    type={type}
                    onFocus={()=>{setActive(true)}}
                    onBlur={handleBlur}
                    className="my-input" id={id} 
                    value={props.Value} 
                    onChange={(e)=>props.setValue(e.target.value)} 
                /> : 
                <input
                    type={type}
                    onFocus={()=>{setActive(true)}}
                    onBlur={handleBlur}
                    className="my-input" id={id} 
                    value={props.Value} 
                    onChange={(e)=>props.setValue(e.target.value)} 
                />
                
            }
             

        </div>
    )
}