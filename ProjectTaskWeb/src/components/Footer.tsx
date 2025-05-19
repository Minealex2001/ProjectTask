import { Box, Container, Typography, Link } from '@mui/material';

export default function Footer() {
  return (
    <Box sx={{ bgcolor: 'grey.900', color: 'grey.100', py: 4, mt: 8 }} component="footer">
      <Container maxWidth="lg" sx={{ textAlign: 'center' }}>
        <Typography variant="body2">
          © {new Date().getFullYear()} ProjectTask. Desarrollado con ♥ usando Kotlin Multiplatform y Compose.
        </Typography>
        <Typography variant="body2" mt={1}>
          <Link href="#contacto" color="inherit" underline="hover">Contacto</Link>
        </Typography>
      </Container>
    </Box>
  );
}
