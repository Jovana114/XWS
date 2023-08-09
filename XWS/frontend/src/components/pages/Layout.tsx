import React, { ReactNode } from "react";
import Navigation from "../common/Navigation/Navigation";

interface LayoutProps {
  children: ReactNode;
}

const Layout: React.FC<LayoutProps> = ({ children }) => {
  return (
    <main className="App">
      <Navigation />
      <div style={{ display: "flex", flex: 1, margin: "20px" }}>
        {children} {/* Render the nested route components */}
      </div>
    </main>
  );
};

export default Layout;
