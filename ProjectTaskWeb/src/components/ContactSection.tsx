import { Box, Button, Container, TextField, Typography, Stack } from '@mui/material';
import EmailIcon from '@mui/icons-material/Email';
import { useState } from 'react';

export default function ContactSection() {
  const [sent, setSent] = useState(false);
  return (
    <Box sx={{ bgcolor: 'background.default', py: 10 }} id="contacto">
      <Container maxWidth="sm" sx={{ textAlign: 'center' }}>
        <Typography variant="h3" component="h2" fontWeight={700} gutterBottom>
          Contacto
        </Typography>
        <Typography variant="body1" color="text.secondary" mb={4}>
          ¿Tienes dudas, sugerencias o quieres colaborar? ¡Escríbenos!
        </Typography>
        {sent ? (
          <Typography variant="h6" color="primary">¡Gracias por tu mensaje!</Typography>
        ) : (
          <form onSubmit={e => { e.preventDefault(); setSent(true); }}>
            <Stack spacing={2}>
              <TextField label="Nombre" required fullWidth />
              <TextField label="Correo electrónico" type="email" required fullWidth />
              <TextField label="Mensaje" required fullWidth multiline rows={4} />
              <Button type="submit" variant="contained" color="primary" startIcon={<EmailIcon />}>Enviar</Button>
            </Stack>
          </form>
        )}
      </Container>
    </Box>
  );
}
