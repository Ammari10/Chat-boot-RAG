import {NavLink, Outlet} from "react-router-dom";
import "../styles/styles.css";
export default function Layout() {
    return (
        <div className="p-m">
            <nav>
                <NavLink className="btn btn-outline-info m-1" to="/home page">Home</NavLink>
                <NavLink  className="btn btn-outline-info m-1" to="/chat boot">Chat</NavLink>
            </nav>
            <main>
                <Outlet></Outlet>
            </main>
        </div>
    );
}
