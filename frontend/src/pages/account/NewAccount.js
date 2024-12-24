import { LoadingButton } from '@mui/lab';
import { Button, Card, Container, Grid, Stack, TextField, Typography } from '@mui/material';
import { useSnackbar } from 'notistack';
import { useState, useEffect } from 'react';
import { Helmet } from 'react-helmet-async';
import { useNavigate } from 'react-router-dom';
import AuthService from '../../services/AuthService';
import HttpService from '../../services/HttpService';

export default function NewAccount() {
  const navigate = useNavigate();
  const { enqueueSnackbar } = useSnackbar();

  // Get customer ID from the logged-in user's profile
  const currentUser = AuthService.getCurrentUser();
  const customerId = currentUser?.customerId;

  const [balance, setBalance] = useState(0); // State to hold balance
  const [loading, setLoading] = useState(false);

  // Redirect to login if customerId is not available
  useEffect(() => {
    if (!customerId) {
      enqueueSnackbar('User not logged in. Please log in again.', { variant: 'error' });
      navigate('/login');
    }
  }, [customerId, enqueueSnackbar, navigate]);

  const handleSubmit = (event) => {
    event.preventDefault();
    setLoading(true);

    // Prepare the request body
    const requestBody = {
      customerId,
      balance,
      status: 'Open',
    };

    // Send the request to create an account
    HttpService.postWithAuth('/account/createAccount', requestBody)
      .then(() => {
        enqueueSnackbar('Account created successfully', { variant: 'success' });
        navigate('/accounts'); // Navigate back to accounts page
      })
      .catch((error) => {
        if (error.response?.data?.errors) {
          error.response?.data?.errors.forEach((e) => enqueueSnackbar(e.message, { variant: 'error' }));
        } else if (error.response?.data?.message) {
          enqueueSnackbar(error.response?.data?.message, { variant: 'error' });
        } else {
          enqueueSnackbar(error.message, { variant: 'error' });
        }
      })
      .finally(() => {
        setLoading(false);
      });
  };

  return (
    <>
      <Helmet>
        <title> New Account | e-Wallet </title>
      </Helmet>
      <Container sx={{ minWidth: '100%' }}>
        <Stack direction="row" alignItems="center" justifyContent="space-between" mb={1}>
          <Typography variant="h4" gutterBottom>
            New Account
          </Typography>
        </Stack>
        <Card>
          <Grid container alignItems="left" justify="center" direction="column" sx={{ width: 400, padding: 5 }}>
            <Stack spacing={3}>
              <TextField
                id="balance"
                name="balance"
                label="Initial Balance"
                autoComplete="balance"
                type="number"
                required
                value={balance}
                onChange={(e) => setBalance(e.target.value)}
              />
            </Stack>
            <Stack spacing={2} direction="row" alignItems="right" justifyContent="end" sx={{ mt: 4 }}>
              <Button sx={{ width: 120 }} variant="outlined" onClick={() => navigate('/accounts')}>
                Cancel
              </Button>
              <LoadingButton
                sx={{ width: 120 }}
                size="large"
                type="submit"
                variant="contained"
                onClick={handleSubmit}
                loading={loading}
              >
                Create
              </LoadingButton>
            </Stack>
          </Grid>
        </Card>
      </Container>
    </>
  );
}
