import { Box, Container, Typography, Grid, Paper } from '@mui/material';

// Usa imágenes de ejemplo, el usuario puede reemplazarlas por capturas reales
const screenshots = [
  {
    src: 'https://mui.com/static/images/cards/contemplative-reptile.jpg',
    alt: 'Vista principal de ProjectTask',
    caption: 'Vista principal: tus tareas organizadas y accesibles.'
  },
  {
    src: 'https://mui.com/static/images/cards/paella.jpg',
    alt: 'Editor de tareas',
    caption: 'Editor de tareas: añade detalles, notas y tiempos estimados.'
  },
  {
    src: 'https://mui.com/static/images/cards/live-from-space.jpg',
    alt: 'Modo oscuro',
    caption: 'Modo oscuro: ideal para trabajar de noche.'
  }
];

export default function ScreenshotsSection() {
  return (
    <Box sx={{ bgcolor: 'background.paper', py: 10 }} id="capturas">
      <Container maxWidth="lg">
        <Typography variant="h3" component="h2" align="center" fontWeight={700} gutterBottom>
          Capturas de pantalla
        </Typography>
        <Grid container spacing={4} mt={2} justifyContent="center">
          {screenshots.map((shot, i) => (
            <Grid item xs={12} sm={6} md={4} key={i}>
              <Paper elevation={2} sx={{ p: 2, textAlign: 'center' }}>
                <img src={shot.src} alt={shot.alt} style={{ width: '100%', borderRadius: 8, marginBottom: 8 }} />
                <Typography variant="body2" color="text.secondary">{shot.caption}</Typography>
              </Paper>
            </Grid>
          ))}
        </Grid>
      </Container>
    </Box>
  );
}
