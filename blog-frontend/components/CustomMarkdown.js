import Markdown from "react-markdown";
import Image from "next/image";

const components = {
    h1: 'h2',
    h2: 'h3',
    h3: 'h4',
    h4: 'h5',
    h5: 'h6',
    h6(props) {
        const {node, ...rest} = props
        return <h6 className="text-secondary" {...props}/>
    },
    img(props) {
        const {node, src, alt, ...rest} = props
        return (
            <div className="position-relative" style={{height: "350px"}}>
                <Image fill={true} src={src} alt={alt} {...props} style={{objectFit: "contain"}}/>
            </div>
        )
    }
}

export default function CustomMarkdown({children, ...rest}) {
    return <Markdown {...rest} components={components}>{children}</Markdown>
}