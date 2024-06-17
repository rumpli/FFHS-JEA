import {useSession} from "@/lib/hooks/session";
import {useAuthRedirect} from "@/lib/hooks/authredirect";
import {Button, Container} from "react-bootstrap";
import Header from "@/components/Header";
import "bootstrap/dist/css/bootstrap.min.css";
import styles from '@/styles/_app.module.css';
import {BsPlus} from "react-icons/bs";
import {useRouter} from "next/router";

export default function App({Component, pageProps}) {
    const {isLoaded, isSignedIn} = useSession()
    const router = useRouter()
    useAuthRedirect(pageProps)
    return (
        <>
            <Header/>

            <Container as="main" className="page pb-5">
                {(!pageProps.secured || isSignedIn) && <Component {...pageProps} />}
                {isSignedIn && <Button size="lg" className={styles.fab}
                         onClick={() => router.push('/posts/create')}><BsPlus/></Button>}
            </Container>
        </>
    )
}
