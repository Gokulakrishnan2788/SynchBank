# Home (Dashboard) — API Layer
# cat with: CONTEXT.md + contract/api_contract.md + this file

## Generate these files ONLY
- DashboardApiService.kt
- DashboardDto.kt (DashboardResponseDto, AccountSummaryDto, ActivityItemDto, ChartDataDto)
- assets/mock/api/dashboard.json
- assets/mock/api/dashboard_chart.json

## Endpoints
GET /dashboard          → DashboardResponseDto
GET /dashboard/chart    → ChartDataDto (accepts ?period=6m|3m|1y)

## DTOs

### DashboardResponseDto
{
  "netWorth": Double,               // 142850.42
  "netWorthChange": String,         // "+2.4%"
  "netWorthChangePositive": Boolean,
  "accounts": [AccountSummaryDto],
  "activity": [ActivityItemDto],
  "exclusiveOffer": ExclusiveOfferDto
}

### AccountSummaryDto
{
  "id": String,
  "type": String,         // "ELITE CHECKING"
  "balance": String,      // "$42,301.15"
  "detail": String,       // "Available • ••• 9012"
  "iconAsset": String
}

### ActivityItemDto
{
  "id": String,
  "title": String,        // "Apple Store"
  "subtitle": String,     // "Electronics • Today"
  "amount": String,       // "-$1,299.00"
  "amountPositive": Boolean,
  "iconAsset": String,
  "iconBgColor": String   // "IconBgGrey" or "IconBgGreen"
}

### ExclusiveOfferDto
{
  "badgeLabel": String,
  "title": String,
  "ctaLabel": String,
  "bgColor": String,
  "illustrationAsset": String
}

### ChartDataDto
{
  "period": String,
  "dataPoints": [Double]
}

## Mock JSON — dashboard.json
{
  "success": true,
  "data": {
    "netWorth": 142850.42,
    "netWorthChange": "+2.4%",
    "netWorthChangePositive": true,
    "accounts": [
      { "id": "acc_001", "type": "ELITE CHECKING", "balance": "$42,301.15", "detail": "Available • ••• 9012", "iconAsset": "ic_account_checking" },
      { "id": "acc_002", "type": "HIGH-YIELD SAVINGS", "balance": "$100,549.27", "detail": "4.50% APY • ••• 4481", "iconAsset": "ic_account_savings" }
    ],
    "activity": [
      { "id": "act_001", "title": "Apple Store", "subtitle": "Electronics • Today", "amount": "-$1,299.00", "amountPositive": false, "iconAsset": "ic_txn_shopping", "iconBgColor": "IconBgGrey" },
      { "id": "act_002", "title": "Salary Deposit", "subtitle": "Income • Oct 15", "amount": "+$8,450.00", "amountPositive": true, "iconAsset": "ic_txn_income", "iconBgColor": "IconBgGreen" },
      { "id": "act_003", "title": "The Oak Bistro", "subtitle": "Dining • Oct 14", "amount": "-$84.20", "amountPositive": false, "iconAsset": "ic_txn_dining", "iconBgColor": "IconBgGrey" },
      { "id": "act_004", "title": "Tesla Supercharger", "subtitle": "Transport • Oct 12", "amount": "-$22.50", "amountPositive": false, "iconAsset": "ic_txn_transport", "iconBgColor": "IconBgGrey" }
    ],
    "exclusiveOffer": {
      "badgeLabel": "EXCLUSIVE OFFER",
      "title": "Unlock Private Wealth Advisory Services",
      "ctaLabel": "Learn more →",
      "bgColor": "NavyPrimary",
      "illustrationAsset": "ic_offer_illustration"
    }
  }
}

## Mock JSON — dashboard_chart.json
{
  "success": true,
  "data": {
    "period": "Last 6 Months",
    "dataPoints": [85000, 92000, 88000, 105000, 118000, 142850]
  }
}

## Rules
- Do NOT create new NetworkModule or Retrofit instance
- Inject DashboardApiService via DashboardModule.kt
