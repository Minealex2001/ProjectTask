import { Box, Container, Grid, Typography, Paper } from '@mui/material';
import CheckCircleIcon from '@mui/icons-material/CheckCircle';
import SecurityIcon from '@mui/icons-material/Security';
import DarkModeIcon from '@mui/icons-material/DarkMode';
import SpeedIcon from '@mui/icons-material/Speed';
import NoteAltIcon from '@mui/icons-material/NoteAlt';
import TranslateIcon from '@mui/icons-material/Translate';

const features = [
  {
    icon: <CheckCircleIcon color="primary" sx={{ fontSize: 40 }} />, title: 'Gestión de tareas',
    desc: 'Crea, edita y organiza tus tareas fácilmente. Marca como completadas y añade notas.'
  },
  {
    icon: <SecurityIcon color="primary" sx={{ fontSize: 40 }} />, title: 'Seguridad',
    desc: 'Tus tareas se guardan cifradas localmente. Privacidad y control total.'
  },
  {
    icon: <DarkModeIcon color="primary" sx={{ fontSize: 40 }} />, title: 'Modo oscuro',
    desc: 'Interfaz moderna y adaptable, ideal para trabajar de día o de noche.'
  },
  {
    icon: <SpeedIcon color="primary" sx={{ fontSize: 40 }} />, title: 'Rápida y ligera',
    desc: 'Desarrollada con Kotlin Multiplatform y Compose, inicia en segundos.'
  },
  {
    icon: <NoteAltIcon color="primary" sx={{ fontSize: 40 }} />, title: 'Notas y recordatorios',
    desc: 'Agrega notas a cada tarea y mantén tus ideas organizadas.'
  },
  {
    icon: <TranslateIcon color="primary" sx={{ fontSize: 40 }} />, title: 'Multilenguaje',
    desc: 'Disponible en español e inglés. Personaliza tu experiencia.'
  },
];

export default function FeaturesSection() {
  return (
    <Box sx={{ bgcolor: 'background.default', py: 10 }} id="caracteristicas">
      <Container maxWidth="lg">
        <Typography variant="h3" component="h2" align="center" fontWeight={700} gutterBottom>
          Características principales
        </Typography>
        <Grid container spacing={4} mt={2}>
          {features.map((f, i) => (
            <Grid item xs={12} sm={6} md={4} key={i}>
              <Paper elevation={3} sx={{ p: 4, textAlign: 'center', height: '100%' }}>
                {f.icon}
                <Typography variant="h6" fontWeight={600} mt={2}>{f.title}</Typography>
                <Typography variant="body1" color="text.secondary" mt={1}>{f.desc}</Typography>
              </Paper>
            </Grid>
          ))}
        </Grid>
      </Container>
    </Box>
  );
}
