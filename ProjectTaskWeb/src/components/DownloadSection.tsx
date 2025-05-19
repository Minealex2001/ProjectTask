import { Box, Button, Container, Typography, Stack } from '@mui/material';
import DownloadIcon from '@mui/icons-material/Download';

export default function DownloadSection() {
  return (
    <Box sx={{ bgcolor: 'secondary.main', color: 'secondary.contrastText', py: 10 }} id="descargar">
      <Container maxWidth="sm" sx={{ textAlign: 'center' }}>
        <Typography variant="h3" component="h2" fontWeight={700} gutterBottom>
          Descarga ProjectTask
        </Typography>
        <Typography variant="h6" component="p" gutterBottom>
          Disponible para Windows. Pronto en más plataformas.
        </Typography>
        <Stack direction="row" spacing={2} justifyContent="center" mt={4}>
          <Button
            variant="contained"
            color="primary"
            size="large"
            startIcon={<DownloadIcon />}
            href="#"
            disabled
          >
            Descargar para Windows
          </Button>
          <Button
            variant="outlined"
            color="inherit"
            size="large"
            href="#"
            disabled
          >
            Otras plataformas
          </Button>
        </Stack>
        <Typography variant="body2" color="secondary.contrastText" mt={4}>
          *Próximamente: Mac, Linux y versión móvil.
        </Typography>
      </Container>
    </Box>
  );
}
