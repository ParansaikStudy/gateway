"use client";

import { useState } from "react";
import { useForm } from "react-hook-form";
import { z } from "zod/v4";
import { zodResolver } from "@hookform/resolvers/zod";
import { Settings, Shield, Trash2, CheckCircle2, Loader2, XCircle } from "lucide-react";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { useKisCredentials, useKisToken } from "@/hooks/use-kis";
import { cn } from "@/lib/utils";

const credentialsSchema = z.object({
  appkey: z.string().min(1, "App Key를 입력해주세요"),
  appsecret: z.string().min(1, "App Secret을 입력해주세요"),
  accountNumber: z.string().min(1, "계좌번호를 입력해주세요"),
  environment: z.enum(["practice", "production"]),
});

type CredentialsForm = z.infer<typeof credentialsSchema>;

export default function SettingsPage() {
  const { isConnected, environment, saveCredentials, clearCredentials } =
    useKisCredentials();
  const {
    data: tokenData,
    isLoading: tokenLoading,
    isError: tokenError,
  } = useKisToken(isConnected);

  const [saving, setSaving] = useState(false);

  const {
    register,
    handleSubmit,
    reset,
    watch,
    formState: { errors },
  } = useForm<CredentialsForm>({
    resolver: zodResolver(credentialsSchema),
    defaultValues: {
      appkey: "",
      appsecret: "",
      accountNumber: "",
      environment: "practice",
    },
  });

  const onSubmit = async (data: CredentialsForm) => {
    setSaving(true);
    await saveCredentials(data);
    reset({ appkey: "", appsecret: "", accountNumber: "", environment: data.environment });
    setSaving(false);
  };

  const handleClear = async () => {
    await clearCredentials();
    reset({
      appkey: "",
      appsecret: "",
      accountNumber: "",
      environment: "practice",
    });
  };

  const selectedEnvironment = watch("environment");

  // Connection status
  const connectionStatus = !isConnected
    ? "disconnected"
    : tokenLoading
      ? "connecting"
      : tokenError
        ? "error"
        : tokenData?.valid
          ? "connected"
          : "disconnected";

  return (
    <div className="mx-auto max-w-2xl space-y-6">
      {/* Header */}
      <div className="flex items-center gap-3">
        <div className="flex h-10 w-10 items-center justify-center rounded-xl bg-primary/10">
          <Settings className="h-5 w-5 text-primary" />
        </div>
        <div>
          <h2 className="text-lg font-semibold text-foreground">
            한국투자증권 API 설정
          </h2>
          <p className="text-sm text-muted-foreground">
            실시간 주가 데이터를 위한 OpenAPI 연동 — 연결 시 WebSocket 실시간(푸시) 시세 제공
          </p>
        </div>
      </div>

      {/* Connection Status */}
      <div
        className={cn(
          "flex items-center gap-2 rounded-lg border px-4 py-3 text-sm",
          connectionStatus === "connected" &&
            "border-emerald-500/30 bg-emerald-500/10 text-emerald-600 dark:text-emerald-400",
          connectionStatus === "connecting" &&
            "border-amber-500/30 bg-amber-500/10 text-amber-600 dark:text-amber-400",
          connectionStatus === "error" &&
            "border-destructive/30 bg-destructive/10 text-destructive",
          connectionStatus === "disconnected" &&
            "border-border bg-muted/50 text-muted-foreground",
        )}
      >
        {connectionStatus === "connected" && (
          <>
            <CheckCircle2 className="h-4 w-4" />
            API 연결됨{environment && ` (${environment === "practice" ? "모의 환경" : "실전 환경"})`}
          </>
        )}
        {connectionStatus === "connecting" && (
          <>
            <Loader2 className="h-4 w-4 animate-spin" />
            연결 확인 중...
          </>
        )}
        {connectionStatus === "error" && (
          <>
            <XCircle className="h-4 w-4" />
            연결 실패 — API 키를 확인해주세요
          </>
        )}
        {connectionStatus === "disconnected" && (
          <>
            <XCircle className="h-4 w-4" />
            연결되지 않음
          </>
        )}
      </div>

      {/* Form */}
      <form
        onSubmit={handleSubmit(onSubmit)}
        className="space-y-5 rounded-xl border border-border bg-card p-6"
      >
        {isConnected && (
          <p className="text-xs text-muted-foreground">
            보안을 위해 저장된 정보는 표시되지 않습니다. 변경하려면 새로
            입력해주세요.
          </p>
        )}

        {/* Environment: which KIS server to connect to (no order API) */}
        <div className="space-y-2">
          <label className="text-sm font-medium text-foreground">
            연결 환경
          </label>
          <div className="flex gap-2">
            <button
              type="button"
              onClick={() =>
                reset({ ...watch(), environment: "practice" })
              }
              className={cn(
                "flex-1 rounded-lg border px-4 py-2.5 text-sm font-medium transition-colors",
                selectedEnvironment === "practice"
                  ? "border-primary bg-primary/10 text-primary"
                  : "border-border text-muted-foreground hover:border-primary/50",
              )}
            >
              모의 환경
            </button>
            <button
              type="button"
              onClick={() =>
                reset({ ...watch(), environment: "production" })
              }
              className={cn(
                "flex-1 rounded-lg border px-4 py-2.5 text-sm font-medium transition-colors",
                selectedEnvironment === "production"
                  ? "border-primary bg-primary/10 text-primary"
                  : "border-border text-muted-foreground hover:border-primary/50",
              )}
            >
              실전 환경
            </button>
          </div>
        </div>

        {/* App Key */}
        <div className="space-y-2">
          <label className="text-sm font-medium text-foreground">
            App Key
          </label>
          <Input
            type="password"
            placeholder="발급받은 App Key를 입력하세요"
            {...register("appkey")}
          />
          {errors.appkey && (
            <p className="text-xs text-destructive">{errors.appkey.message}</p>
          )}
        </div>

        {/* App Secret */}
        <div className="space-y-2">
          <label className="text-sm font-medium text-foreground">
            App Secret
          </label>
          <Input
            type="password"
            placeholder="발급받은 App Secret을 입력하세요"
            {...register("appsecret")}
          />
          {errors.appsecret && (
            <p className="text-xs text-destructive">
              {errors.appsecret.message}
            </p>
          )}
        </div>

        {/* Account Number */}
        <div className="space-y-2">
          <label className="text-sm font-medium text-foreground">
            계좌번호
          </label>
          <Input
            type="password"
            placeholder="계좌번호 (예: 50123456-01)"
            {...register("accountNumber")}
          />
          {errors.accountNumber && (
            <p className="text-xs text-destructive">
              {errors.accountNumber.message}
            </p>
          )}
        </div>

        {/* Actions */}
        <div className="flex gap-3 pt-2">
          <Button type="submit" disabled={saving} className="flex-1">
            {saving ? <Loader2 className="h-4 w-4 animate-spin" /> : null}
            {isConnected ? "변경" : "저장"}
          </Button>
          {isConnected && (
            <Button
              type="button"
              variant="outline"
              onClick={handleClear}
              className="gap-2"
            >
              <Trash2 className="h-4 w-4" />
              초기화
            </Button>
          )}
        </div>
      </form>

      {/* Security Notice */}
      <div className="flex gap-3 rounded-xl border border-border bg-muted/30 p-4">
        <Shield className="mt-0.5 h-5 w-5 shrink-0 text-muted-foreground" />
        <div className="space-y-1">
          <p className="text-sm font-medium text-foreground">보안 안내</p>
          <p className="text-xs leading-relaxed text-muted-foreground">
            API 키와 계좌번호는 서버에서 AES-256으로 암호화되어 httpOnly
            쿠키에 저장됩니다. 클라이언트(브라우저 JavaScript)에서는 접근할 수
            없으며, API 호출 시에만 서버에서 복호화하여 사용됩니다. 공용 PC에서는
            사용 후 반드시 초기화해주세요.
          </p>
        </div>
      </div>
    </div>
  );
}
