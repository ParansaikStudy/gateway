package com.zqksk.mail.model;

import lombok.Builder;

public class VerificationMail extends Mail {

    @Builder
    public VerificationMail(String recipient, String verificationCode) {
        super(
                recipient,
                "[인증] 게이트웨이에 가입해주셔서 감사합니다.",
                "<table class=\"wrapper\" style=\"border-collapse: collapse;table-layout: fixed;min-width: 320px;width: 100%;background-color: #f0f0f0;\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\">" +
                        "<tbody>" +
                        "<tr>" +
                        "<td>" +
                        "  <div role=\"banner\">" +
                        "    <!-- 로고 영역 -->" +
                        "    <div class=\"header\" style=\"Margin: 0 auto;max-width: 600px;min-width: 320px; width: 320px;width: calc(28000% - 167400px);\" id=\"emb-email-header-container\">" +
                        "      <div class=\"logo\" style=\"font-size: 26px;line-height: 32px;Margin-top: 6px;Margin-bottom: 20px;color: #c3ced9;font-family: Roboto,Tahoma,sans-serif;Margin-left: 20px;Margin-right: 20px;\" align=\"center\">" +
                        "        <div class=\"logo-center\" align=\"center\" id=\"emb-email-header\">" +
                        "          <!-- cid:logoImage 로 대체되는 실제 로고 이미지 -->" +
                        "          <img style=\"display: block;height: auto;width: 100%;border: 0;max-width: 200px;\" src=\"cid:logoImage\" alt=\"게이트웨이\" />" +
                        "        </div>" +
                        "      </div>" +
                        "    </div>" +
                        "  </div>" +
                        "  <div>" +
                        "    <!-- 내용 영역 -->" +
                        "    <div class=\"layout one-col fixed-width stack\" style=\"Margin: 0 auto;max-width: 600px;min-width: 320px; " +
                        "         width: 320px;width: calc(28000% - 167400px);overflow-wrap: break-word;word-wrap: break-word;word-break: break-word;\">" +
                        "      <div class=\"layout__inner\" style=\"border-collapse: collapse;display: table;width: 100%;background-color: #ffffff;\">" +
                        "        <div class=\"column\" style=\"text-align: left;color: #787778;font-size: 16px;line-height: 24px;font-family: Ubuntu,sans-serif;\">" +
                        "          <!-- 상단 여백 -->" +
                        "          <div style=\"Margin-left: 20px;Margin-right: 20px;Margin-top: 24px;\">" +
                        "            <div style=\"mso-line-height-rule: exactly;line-height: 5px;font-size: 1px;\">&nbsp;</div>" +
                        "          </div>" +

                        "          <!-- 환영 문구 -->" +
                        "          <div style=\"Margin-left: 20px;Margin-right: 20px;\">" +
                        "            <div style=\"mso-line-height-rule: exactly;mso-text-raise: 11px;vertical-align: middle;\">" +
                        "              <h1 style=\"Margin-top: 0;Margin-bottom: 0;font-style: normal;font-weight: normal;" +
                        "                  color: #565656;font-size: 28px;line-height: 36px;text-align: center;\">" +
                        "                  <strong>회원가입을 환영합니다!</strong>" +
                        "              </h1>" +
                        "              <p style=\"Margin-top: 20px;Margin-bottom: 20px;text-align: center;\">" +
                        "                안녕하세요.<br/>" +
                        "                <strong>게이트웨이</strong>에 가입해 주셔서 감사합니다.<br/>" +
                        "                <br/>" +
                        "                아래 <span style=\"color: #f70a3d; font-weight: bold;\">4자리 인증코드</span>를 " +
                        "                확인하셔서 회원가입을 완료해 주세요.<br/>" +
                        "              </p>" +
                        "            </div>" +
                        "          </div>" +

                        "          <!-- 인증 코드 강조 -->" +
                        "          <div style=\"Margin-left: 20px;Margin-right: 20px;\">" +
                        "            <div style=\"text-align: center; margin-bottom: 20px;\">" +
                        "              <span style=\"display: inline-block;padding: 12px 24px;border-radius: 4px;" +
                        "                    background-color: #1c4165;color: #ffffff;font-size: 24px;font-weight: bold;\">" +
                        "                " + verificationCode +
                        "              </span>" +
                        "            </div>" +
                        "          </div>" +

                        "          <!-- 추가 안내 -->" +
                        "          <div style=\"Margin-left: 20px;Margin-right: 20px;\">" +
                        "            <div style=\"mso-line-height-rule: exactly;mso-text-raise: 11px;vertical-align: middle;\">" +
                        "              <p style=\"Margin-top: 0;Margin-bottom: 20px;text-align: center;\">" +
                        "                위 인증코드를 회원가입 화면에 입력해 주세요.<br/>" +
                        "                문제가 발생할 경우, <br/>" +
                        "                <strong></strong>에게 문의해 주시기 바랍니다.<br/>" +
                        "              </p>" +
                        "            </div>" +
                        "          </div>" +

                        "          <!-- 하단 여백 -->" +
                        "          <div style=\"Margin-left: 20px;Margin-right: 20px;Margin-bottom: 24px;\">" +
                        "            <div style=\"mso-line-height-rule: exactly;line-height: 5px;font-size: 1px;\">&nbsp;</div>" +
                        "          </div>" +
                        "        </div>" +
                        "      </div>" +
                        "    </div>" +

                        "    <!-- 푸터(문의 안내) 영역 -->" +
                        "    <div style=\"mso-line-height-rule: exactly;line-height: 10px;font-size: 10px;\">&nbsp;</div>" +
                        "    <div class=\"layout one-col fixed-width stack\" " +
                        "         style=\"Margin: 0 auto;max-width: 600px;min-width: 320px; width: 320px;" +
                        "                width: calc(28000% - 167400px);overflow-wrap: break-word;word-wrap: break-word;word-break: break-word;\">" +
                        "      <div class=\"layout__inner\" style=\"border-collapse: collapse;display: table;width: 100%;background-color: #f0f0f0;\">" +
                        "        <div class=\"column\" style=\"text-align: left;color: #787778;font-size: 16px;line-height: 24px;font-family: Ubuntu,sans-serif;\">" +
                        "          <div style=\"Margin-left: 20px;Margin-right: 20px;Margin-top: 24px;Margin-bottom: 24px;\">" +
                        "            <div style=\"mso-line-height-rule: exactly;mso-text-raise: 11px;vertical-align: middle;\">" +
                        "              <p style=\"Margin-top: 0;Margin-bottom: 0;text-align: center;\">" +
                        "                본 이메일은 발신 전용입니다. 문의사항이 있으시면 <br/>" +
                        "                <u><strong></strong></u>에게 문의해주세요." +
                        "              </p>" +
                        "            </div>" +
                        "          </div>" +
                        "        </div>" +
                        "      </div>" +
                        "    </div>" +

                        "  </div>" +
                        "</td>" +
                        "</tr>" +
                        "</tbody>" +
                        "</table>"
        );
    }
}
