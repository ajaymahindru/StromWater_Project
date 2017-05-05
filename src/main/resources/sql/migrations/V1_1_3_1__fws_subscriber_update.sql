-- replacing FWS contact tanya_sommer@fws.gov with patrick_connor@fws.gov
UPDATE CGP_REF_SUBSCRIBERS
  SET SUBSCRIBER_EMAIL='patrick_connor@fws.gov',
    FIRST_NAME='Patrick',
    LAST_NAME='Connor'
  WHERE ID=33;




