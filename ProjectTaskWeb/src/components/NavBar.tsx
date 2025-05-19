import { AppBar, Box, Toolbar, Typography, Button, Container, IconButton } from '@mui/material';
import RocketLaunchIcon from '@mui/icons-material/RocketLaunch';

export default function NavBar() {
  return (
    <AppBar position="sticky" color="default" elevation={1}>
      <Container maxWidth="lg">
        <Toolbar disableGutters>
          <IconButton href="#" size="large" edge="start" color="primary" sx={{ mr: 2 }}>
            <RocketLaunchIcon />
          </IconButton>
          <Typography variant="h6" component="div" sx={{ flexGrow: 1, fontWeight: 700 }}>
            ProjectTask
          </Typography>
          <Box>
            <Button color="primary" href="#caracteristicas">Características</Button>
            <Button color="primary" href="#capturas">Capturas</Button>
            <Button color="primary" href="#descargar">Descargar</Button>
            <Button color="primary" href="#contacto">Contacto</Button>
          </Box>
        </Toolbar>
      </Container>
    </AppBar>
  );
}
