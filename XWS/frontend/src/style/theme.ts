import { createTheme } from '@mui/material/styles';

const theme = createTheme({
  palette: {
    primary: { 
      main: '#373F51', // Custom primary color D8DBE2
    },
    secondary: {
      main: '#A9BCD0', // Custom secondary color
    },
    text: {
      primary: '#1B1B1E', // Custom text color
    },
    error: {
      main: '#FF0000', // Custom error color (Red)
    },
    warning: {
      main: '#FFA726', // Custom warning color (Orange)
    },
    info: {
      main: '#00BFFF', // Custom info color (Blue)
    },
    success: {
      main: '#008000', // Custom success color (Green)
    },
    mode: 'light', // Choose 'light' or 'dark' mode
    tonalOffset: 0.2, // Tonal offset for color shades
    contrastThreshold: 3, // Contrast threshold for text against background
    divider: '#D8DBE2', // Custom divider color (same as primary color)
    background: {
      paper: '#FFFFFF', // Custom background color for paper surfaces (White)
      default: '#1B1B1E', // Custom background color for default surfaces (Grey)
    },
  },
});

export default theme;
