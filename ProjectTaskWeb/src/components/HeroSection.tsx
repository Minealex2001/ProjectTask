import { Box, Button, Container, Typography } from '@mui/material';
import RocketLaunchIcon from '@mui/icons-material/RocketLaunch';

export default function HeroSection() {
  return (
    <Box sx={{ bgcolor: 'primary.main', color: 'primary.contrastText', py: 10 }}>
      <Container maxWidth="md" sx={{ textAlign: 'center' }}>
        <RocketLaunchIcon sx={{ fontSize: 64, mb: 2 }} />
        <Typography variant="h2" component="h1" gutterBottom fontWeight={700}>
          ProjectTask
        </Typography>
        <Typography variant="h5" component="p" gutterBottom>
          Organiza, gestiona y motívate con tu lista de tareas multiplataforma. ¡Productividad y simplicidad en una sola app!
        </Typography>
        <Button
          variant="contained"
          color="secondary"
          size="large"
          href="#descargar"
          sx={{ mt: 4 }}
        >
          Descargar ahora
        </Button>
      </Container>
    </Box>
  );
}
