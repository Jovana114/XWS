import * as React from 'react';
import AppBar from '@mui/material/AppBar';
import Box from '@mui/material/Box';
import Toolbar from '@mui/material/Toolbar';
import IconButton from '@mui/material/IconButton';
import Typography from '@mui/material/Typography';
import Menu from '@mui/material/Menu';
import MenuIcon from '@mui/icons-material/Menu';
import Container from '@mui/material/Container';
import Avatar from '@mui/material/Avatar';
import Button from '@mui/material/Button';
import Tooltip from '@mui/material/Tooltip';
import MenuItem from '@mui/material/MenuItem';
import CreateFlight  from "../Flights/CreateFlight";
import SignUpHosts  from "../signup/SignUpHosts";
import { Navigate } from "react-router-dom";


const pages = ['Create flight', 'Sign up hosts'];
const settings = ['Logout'];

export const ResponsiveAppBarHost = () => {

  const [openCreateFlight, setOpenCreateFlight] = React.useState(false);
  const [openSignUpHosts, setOpenSignUpHosts] = React.useState(false);
  const [navigate, setNavigate] = React.useState(false);
  const [navigatePage, setNavigatePage] = React.useState("");

  const [anchorElNav, setAnchorElNav] = React.useState<null | HTMLElement>(null);
  const [anchorElUser, setAnchorElUser] = React.useState<null | HTMLElement>(null);

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

  const handleClickOpenCreateFlight = () => {
    setOpenCreateFlight(true);
  };

  const handleCloseCreateFlight = () => {
    setOpenCreateFlight(false);
  };

  const handleClickOpenSignUpHosts = () => {
    setOpenSignUpHosts(true);
  };

  const handleCloseSignUpHosts = () => {
    setOpenSignUpHosts(false);
  };

  const handlePages = (selectedPage: string) => {
    handleCloseUserMenu();
    if (selectedPage === "Create flight") {
      handleClickOpenCreateFlight();
    } else if (selectedPage === "Logout") {
      logout();
    } else if (selectedPage === "Sign up hosts") {
      handleClickOpenSignUpHosts();
    }
  };

  if (navigate) {
    return <Navigate to="/login" />;
  }

  const logout = async () => {
    localStorage.removeItem("token");
    localStorage.removeItem("id");
    localStorage.removeItem("role");
    setNavigate(true);
  };

  return (
    <>
      <AppBar position="static">
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
                {pages.map((page: any) => (
                  <MenuItem key={page} onClick={() => handlePages(page)}>
                    <Typography textAlign="center">{page}</Typography>
                  </MenuItem>
                ))}
              </Menu>
            </Box>
            {pages.length > 0 ? (
              <Box sx={{ flexGrow: 1, display: { xs: "none", md: "flex" } }}>
                {pages.map((page: any) => (
                  <Button
                    key={page}
                    onClick={() => handlePages(page)}
                    sx={{ my: 2, color: "white", display: "block" }}
                  >
                    {page}
                  </Button>
                ))}
              </Box>
            ) : (
              <></>
            )}

            <Box sx={{ flexGrow: 0 }}>
              <Tooltip title="Open settings">
                <IconButton onClick={handleOpenUserMenu} sx={{ p: 0 }}>
                  <Avatar alt="Remy Sharp" src="/img/avatar.png" />
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
                    onClick={() => handlePages(setting)}
                  >
                    <Typography textAlign="center">{setting}</Typography>
                  </MenuItem>
                ))}
              </Menu>
            </Box>
          </Toolbar>
        </Container>
      </AppBar>
    <CreateFlight open={openCreateFlight} onClose={handleCloseCreateFlight} />
    <SignUpHosts open={openSignUpHosts} onClose={handleCloseSignUpHosts} />
    </>
  );
};