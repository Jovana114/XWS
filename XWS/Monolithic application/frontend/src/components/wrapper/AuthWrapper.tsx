import React, { ReactNode } from "react";

interface WrapperProps {
  children: ReactNode;
}

const AuthWrapper = ({ children }: WrapperProps) => (
  <div
    style={{
      display: "flex",
      flexDirection: "row",
      width: "100vw",
      height: "100vh",
    }}
  >
    <img
      style={{ width: "70%", height: "100%", objectFit: "cover" }}
      src="/images/background.jpg"
      alt="plane"
    />
    <div
      style={{
        display: "flex",
        width: "100%",
        height: "400px",
        alignItems: "center",
        margin: "auto 0",
      }}
    >
      {children}
    </div>
  </div>
);

export default AuthWrapper;