import styled from "styled-components";

export const ParentContainerStyles = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  height: 100vh;
  width: 100%;
  margin: 0 auto;
`;

export const UpperRowStyles = styled.div`
  display: flex;
  justify-content: space-around;
  width: 100%;
`;

export const ColumnStyles = styled.div`
  flex: 1;
  padding: 20px;
`;

export const CenteredRowStyles = styled.div`
  text-align: center;
  width: 100%;
`;

export const CenteredColumnStyles = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px;
  border: 1px solid #ccc;
`;

export const LowerRowContainerStyles = styled.div`
  border-top: 1px solid black;
  width: 100%;
  margin-top: 8px;
`;

export const LowerRowStyles = styled.div`
  display: flex;
  justify-content: space-around;
  align-items: center;
  width: 100%;
`;

export const LowerColumnStyles = styled.div`
  flex: 1;
  padding: 20px;
  max-width: 300px;
`;

export const Header = styled.header`
  text-align: center;
`;

export const Main = styled.main`
  display: flex;
  flex-direction: column;
  & .MuiTextField-root {
    margin-bottom: 1rem;
    width: 300px;
  }
`;

export const Footer = styled.footer`
  margin-top: 8px;
  width: 300px;
`;
