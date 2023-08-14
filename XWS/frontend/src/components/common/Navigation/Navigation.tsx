import * as React from "react";
import AppBar from "@mui/material/AppBar";
import Box from "@mui/material/Box";
import Toolbar from "@mui/material/Toolbar";
import IconButton from "@mui/material/IconButton";
import Typography from "@mui/material/Typography";
import Menu from "@mui/material/Menu";
import MenuIcon from "@mui/icons-material/Menu";
import Container from "@mui/material/Container";
import Avatar from "@mui/material/Avatar";
import Button from "@mui/material/Button";
import Tooltip from "@mui/material/Tooltip";
import MenuItem from "@mui/material/MenuItem";

import { ThemeProvider, styled } from "@mui/material/styles";
import { Link, useNavigate } from "react-router-dom";
import useAuth from "../../../hooks/useAuth";
import { AuthContext } from "../../../auth/AuthContext";
import { useContext } from "react";
import { useTheme } from "@mui/styles";

const settings = ["Profile", "Logout"];

const unauthenticatedLinks = [
  { to: "/", label: "Home" },
  { to: "/signin", label: "Sign In" },
  { to: "/signup", label: "Sign Up" },
];

const guestLinks = [
  { to: "/", label: "Home" },
  { to: "/users", label: "Users" },
];

const hostLinks = [
  { to: "/", label: "Home" },
  { to: "/host", label: "Host" },
];

styled(AppBar)(({ theme }) => ({
  background: theme.palette.background.default,
}));

function Navigation() {
  const theme = useTheme(); // Use the useTheme hook to access the theme object

  const { setAuth }: any = useContext(AuthContext);
  const auth = useAuth();
  const navigate = useNavigate();

  const userLinks = auth.auth.accessToken
    ? auth.auth.roles?.includes("ROLE_HOST")
      ? hostLinks
      : guestLinks
    : unauthenticatedLinks;

  const [anchorElNav, setAnchorElNav] = React.useState<null | HTMLElement>(
    null
  );
  const [anchorElUser, setAnchorElUser] = React.useState<null | HTMLElement>(
    null
  );

  const logout = async () => {
    setAuth({});
    location.reload();
  };

  const handleOpenNavMenu = (event: React.MouseEvent<HTMLElement>) => {
    setAnchorElNav(event.currentTarget);
  };
  const handleOpenUserMenu = (event: React.MouseEvent<HTMLElement>) => {
    setAnchorElUser(event.currentTarget);
  };

  const handleCloseNavMenu = () => {
    setAnchorElNav(null);
  };

  const handleCloseUserMenu = () => {
    setAnchorElUser(null);
  };

  const handleSettingsUserMenu = (setting: any) => {
    setAnchorElUser(null);
    if (setting === "Logout") {
      logout();
    }
    if (setting === "Profile") {
      navigate("/profile");
    }
  };

  return (
    <ThemeProvider theme={theme}>
      <AppBar position="fixed">
        <Container maxWidth="xl">
          <Toolbar disableGutters>
            <Box sx={{ flexGrow: 1, display: { xs: "flex", md: "none" } }}>
              <IconButton
                size="large"
                aria-label="account of current user"
                aria-controls="menu-appbar"
                aria-haspopup="true"
                onClick={handleOpenNavMenu}
                color="inherit"
              >
                <MenuIcon />
              </IconButton>
              <Menu
                id="menu-appbar"
                anchorEl={anchorElNav}
                anchorOrigin={{
                  vertical: "bottom",
                  horizontal: "left",
                }}
                keepMounted
                transformOrigin={{
                  vertical: "top",
                  horizontal: "left",
                }}
                open={Boolean(anchorElNav)}
                onClose={handleCloseNavMenu}
                sx={{
                  display: { xs: "block", md: "none" },
                }}
              >
                {userLinks.map((link) => (
                  <Link
                    key={link.to} // Add the key prop here
                    style={{ color: "unset", textDecoration: "none" }}
                    to={link.to}
                  >
                    <MenuItem key={link.to} onClick={handleCloseNavMenu}>
                      {link.label}
                    </MenuItem>
                  </Link>
                ))}
              </Menu>
            </Box>

            <Box sx={{ flexGrow: 1, display: { xs: "none", md: "flex" } }}>
              {userLinks.map((link) => (
                <Link
                  key={link.to} // Add the key prop here
                  style={{ color: "unset", textDecoration: "none" }}
                  to={link.to}
                >
                  <Button
                    onClick={handleCloseNavMenu}
                    sx={{ my: 2, color: "white", display: "block" }}
                  >
                    {link.label}
                  </Button>
                </Link>
              ))}
            </Box>
            {auth.auth.accessToken && (
              <Box sx={{ flexGrow: 0 }}>
                <Tooltip title="Open settings">
                  <IconButton onClick={handleOpenUserMenu} sx={{ p: 0 }}>
                    <Avatar alt="" />
                  </IconButton>
                </Tooltip>
                <Menu
                  sx={{ mt: "45px" }}
                  id="menu-appbar"
                  anchorEl={anchorElUser}
                  anchorOrigin={{
                    vertical: "top",
                    horizontal: "right",
                  }}
                  keepMounted
                  transformOrigin={{
                    vertical: "top",
                    horizontal: "right",
                  }}
                  open={Boolean(anchorElUser)}
                  onClose={handleCloseUserMenu}
                >
                  {settings.map((setting) => (
                    <MenuItem
                      key={setting}
                      onClick={() => {
                        handleSettingsUserMenu(setting);
                      }}
                    >
                      <Typography textAlign="center">{setting}</Typography>
                    </MenuItem>
                  ))}
                </Menu>
              </Box>
            )}
          </Toolbar>
        </Container>
      </AppBar>
    </ThemeProvider>
  );
}
export default Navigation;
