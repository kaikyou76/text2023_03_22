CASE
    WHEN tmp_ad.position = '_社員' THEN '1'
    WHEN tmp_ad.position = '_派遣社員' THEN '0'
END AS fulltime_employee,
tmp_integratedid_employee.employee_cd AS login_id,
tmp_integratedid_employee.employee_cd AS biz_employee_id,
tmp_integratedid_employee.employee_cd AS cucm_login_id,
tmp_integratedid_employee.employee_nm_kanji,
tmp_integratedid_employee.employee_nm_kana,
tmp_integratedid_employee.birthday,
COALESCE(tmp_ad.first_nm, tmp_integratedid_employee.employee_cd) AS first_name,
COALESCE(tmp_ad.last_nm, tmp_integratedid_employee.employee_cd) AS last_name

