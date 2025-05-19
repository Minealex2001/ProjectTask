import { CssBaseline, ThemeProvider, createTheme } from '@mui/material';
import NavBar from './components/NavBar';
import HeroSection from './components/HeroSection';
import FeaturesSection from './components/FeaturesSection';
import ScreenshotsSection from './components/ScreenshotsSection';
import DownloadSection from './components/DownloadSection';
import ContactSection from './components/ContactSection';
import Footer from './components/Footer';

const theme = createTheme({
  palette: {
    mode: 'dark',
    primary: { main: '#37BF6E' },
    secondary: { main: '#3870B2' },
  },
  typography: {
    fontFamily: 'Roboto, Arial, sans-serif',
  },
});

function App() {
  return (
    <ThemeProvider theme={theme}>
      <CssBaseline />
      <NavBar />
      <HeroSection />
      <FeaturesSection />
      <ScreenshotsSection />
      <DownloadSection />
      <ContactSection />
      <Footer />
    </ThemeProvider>
  );
}

export default App;
