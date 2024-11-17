INSERT INTO public.customer (customer_id, customer_name, customer_phone_number, username, password)
VALUES (1, 'John Doe', '0000000000', 'johndoe', '$2a$10$MMOkMuO8zVcXl8YH2GrZSOYf/9zeC/sznGHRVzAq0T8.tzet7QJWq');
INSERT INTO public.customer (customer_id, customer_name, customer_phone_number, username, password)
VALUES (2, 'Linda Calvin', '1111111111', 'lindacalvin', '$2a$10$I5hOscIFqw73AU2/my0H0.vHAjI/rxXGcI49PB/jl8krTcM7VqkCy');
INSERT INTO public.customer (customer_id, customer_name, customer_phone_number, username, password)
VALUES (3, 'Jeffrey Taylor', '2222222222', 'jeffreytaylor', '$2a$10$0grDMvQ7mSRLDAS6zuGOp.0ycwhgAzyE2FgLHzCV8KaXXP2TtGJ/W');

SELECT setval('customer_seq', max(customer_id)) FROM public.customer;