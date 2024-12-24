import { LoadingButton } from '@mui/lab';
import { Autocomplete, Button, Card, Grid, Stack, TextField } from '@mui/material';
import { useSnackbar } from 'notistack';
import { useEffect, useState } from 'react';
import { Helmet } from 'react-helmet-async';
import { useNavigate } from 'react-router-dom';
import AuthService from '../../services/AuthService';
import HttpService from '../../services/HttpService';

export default function AddFunds() {
  const defaultValues = {
    accountId: '',
    amount: '',
  };

  const navigate = useNavigate();
  const { enqueueSnackbar } = useSnackbar();
  const [formValues, setFormValues] = useState(defaultValues);
  const [accounts, setAccounts] = useState([]);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormValues({
      ...formValues,
      [name]: value,
    });
  };

  useEffect(() => {
    const userId = AuthService.getCurrentUser()?.customerId;
    if (userId) {
      HttpService.getWithAuth(`account/getUserAccounts?userId=${userId}`)
        .then((response) => {
          if (response && Array.isArray(response)) {
            setAccounts(response); // Ensure the response is set correctly
          } else {
            enqueueSnackbar('No accounts found for this user.', { variant: 'warning' });
          }
        })
        .catch((error) => {
          enqueueSnackbar(error.response?.data?.message || error.message, { variant: 'error' });
        });
    }
  }, []);  

  const handleSubmit = (event) => {
    event.preventDefault();
    HttpService.putWithAuth('account/addMoney', formValues)
      .then(() => {
        enqueueSnackbar('Funds added successfully', { variant: 'success' });
        navigate('/transactions');
      })
      .catch((error) => {
        enqueueSnackbar(error.response?.data?.message || error.message, { variant: 'error' });
      });
  };

  return (
    <>
      <Helmet>
        <title> Add Funds | e-Wallet </title>
      </Helmet>
      <Card>
        <Grid container direction="column" sx={{ width: 400, padding: 5 }}>
          <Stack spacing={3}>
          <Autocomplete
            options={accounts}
            getOptionLabel={(account) => `Account ID: ${account.accountId} | Balance: ${account.balance}`}
            onChange={(event, newValue) =>
              setFormValues({ ...formValues, accountId: newValue?.accountId || '' })
            }
            renderInput={(params) => <TextField {...params} label="Select Account" />}
            noOptionsText="No accounts available"
          />
            <TextField
              id="amount"
              name="amount"
              label="Amount"
              required
              value={formValues.amount}
              onChange={handleInputChange}
            />
          </Stack>
          <Stack spacing={2} direction="row" justifyContent="end" sx={{ mt: 4 }}>
            <Button variant="outlined" onClick={() => navigate('/accounts')}>
              Cancel
            </Button>
            <LoadingButton variant="contained" onClick={handleSubmit}>
              Add Funds
            </LoadingButton>
          </Stack>
        </Grid>
      </Card>
    </>
  );
}
