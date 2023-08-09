import CircularProgress from "@mui/material/CircularProgress";
import Box from "@mui/material/Box";

const CircularIndeterminate = () => {
  return (
    <Box
      sx={{
        display: "flex",
        justifyContent: "center", // Center horizontally
        alignItems: "center", // Center vertically
        height: "100vh", // Set the container height to full viewport height
        width: "100vw",
      }}
    >
      <CircularProgress />
    </Box>
  );
};

export default CircularIndeterminate;
