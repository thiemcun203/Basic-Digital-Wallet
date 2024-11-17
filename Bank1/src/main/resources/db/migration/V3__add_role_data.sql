INSERT INTO public.role (role_id, "type") VALUES (1, 'ROLE_USER');
INSERT INTO public.role (role_id, "type") VALUES (2, 'ROLE_ADMIN');

SELECT setval('role_seq', max(role_id)) FROM public.role;