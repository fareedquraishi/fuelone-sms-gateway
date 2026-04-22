-- ============================================================
-- FuelOne SMS Gateway — Supabase Schema
-- Run this in the Supabase SQL Editor
-- ============================================================

-- Create message_log table (if it doesn't exist)
CREATE TABLE IF NOT EXISTS public.message_log (
    id              uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    direction       text NOT NULL DEFAULT 'outbound',     -- 'inbound' | 'outbound'
    phone           text,
    customer_mobile text,                                  -- alias for compatibility
    message         text,
    status          text DEFAULT 'pending',               -- sent|failed|delivered|received|pending
    customer_name   text DEFAULT '',
    message_id      text,
    gateway_mode    text,                                  -- WIFI|USB|BLUETOOTH
    keyword_matched text,
    auto_reply_sent boolean DEFAULT false,
    delivered_at    timestamptz,
    read_at         timestamptz,
    thread_id       text,
    created_at      timestamptz DEFAULT now()
);

-- Add new columns to existing table (safe — IF NOT EXISTS)
ALTER TABLE public.message_log
    ADD COLUMN IF NOT EXISTS direction       text DEFAULT 'outbound',
    ADD COLUMN IF NOT EXISTS gateway_mode    text,
    ADD COLUMN IF NOT EXISTS keyword_matched text,
    ADD COLUMN IF NOT EXISTS auto_reply_sent boolean DEFAULT false,
    ADD COLUMN IF NOT EXISTS delivered_at    timestamptz,
    ADD COLUMN IF NOT EXISTS read_at         timestamptz,
    ADD COLUMN IF NOT EXISTS thread_id       text;

-- Indexes for fast inbox queries
CREATE INDEX IF NOT EXISTS idx_message_log_direction
    ON public.message_log(direction, created_at DESC);

CREATE INDEX IF NOT EXISTS idx_message_log_phone
    ON public.message_log(phone, created_at DESC);

CREATE INDEX IF NOT EXISTS idx_message_log_message_id
    ON public.message_log(message_id);

CREATE INDEX IF NOT EXISTS idx_message_log_status
    ON public.message_log(status, created_at DESC);

-- Row Level Security (enable RLS and allow anon insert)
ALTER TABLE public.message_log ENABLE ROW LEVEL SECURITY;

-- Allow anonymous key to insert (gateway writes)
CREATE POLICY IF NOT EXISTS "Allow anon insert"
    ON public.message_log FOR INSERT
    TO anon
    WITH CHECK (true);

-- Allow anonymous key to select (dashboard reads)
CREATE POLICY IF NOT EXISTS "Allow anon select"
    ON public.message_log FOR SELECT
    TO anon
    USING (true);

-- Allow anonymous key to update (delivery status)
CREATE POLICY IF NOT EXISTS "Allow anon update"
    ON public.message_log FOR UPDATE
    TO anon
    USING (true);

-- ─── Useful Views ──────────────────────────────────────────────────────────

-- Inbox view — inbound messages only, newest first
CREATE OR REPLACE VIEW public.message_inbox AS
    SELECT * FROM public.message_log
    WHERE direction = 'inbound'
    ORDER BY created_at DESC;

-- Today's outbound summary
CREATE OR REPLACE VIEW public.outbound_today AS
    SELECT
        COUNT(*) FILTER (WHERE status = 'sent')      AS sent,
        COUNT(*) FILTER (WHERE status = 'delivered') AS delivered,
        COUNT(*) FILTER (WHERE status = 'failed')    AS failed,
        COUNT(*) FILTER (WHERE status = 'pending')   AS pending
    FROM public.message_log
    WHERE direction = 'outbound'
      AND created_at >= CURRENT_DATE;
