import React, { ReactNode } from "react";
import Navigation from "../common/Navigation/Navigation";
import { ToastContainer } from "react-toastify";
import useAuth from "../../hooks/useAuth";
import Loader from "../common/Loader/Loader";

interface LayoutProps {
  children: ReactNode;
}

const Layout: React.FC<LayoutProps> = ({ children }) => {
  const { loading } = useAuth();
  return (
    <main className="App">
      <ToastContainer />
      <Navigation />
      <div
        style={{ display: "flex", flex: 1, margin: "20px", marginTop: "100px" }}
      >
        <div className="full-page-container">
          <section className="center-content">
            {loading ? <Loader /> : <>{children}</>}
          </section>
        </div>
      </div>
    </main>
  );
};

export default Layout;
