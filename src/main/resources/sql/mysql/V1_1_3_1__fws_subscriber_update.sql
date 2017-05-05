-- replacing FWS contact tanya_sommer@fws.gov with patrick_connor@fws.gov
UPDATE cgp_ref_subscribers
  SET subscriber_email='patrick_connor@fws.gov',
    first_name='Patrick',
    last_name='Connor'
  WHERE id=33;




