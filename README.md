# HomeHub - Your Personal Household Management System #

Living in a busy household can be chaotic, but with HomeHub, managing your home has never been easier. HomeHub is a comprehensive household management system designed to streamline your daily tasks, keep everyone organized, 
and ensure that nothing falls through the cracks.

## Shared Shopping Lists ##
Easily create and share shopping lists with all household members. Add items, check them off as you shop, and keep everyone in the loop about what needs to be purchased.
Everything is synchronized with everyone in your household! No more confusion about buying things twice or not at all!

## Task Management ##
Busy day ahead? Cleaning, maintenance, shopping, and other household tasks? Confused about who is responsible for what? HomeHub allows you to assign tasks to specific household members, set deadlines, and track progress.

## Schedule and Calendar ##
Keep track of important dates, appointments, and events with a shared calendar. Sync it with your
personal calendars to ensure everyone is on the same page.

## Shared Finance Tracking ##
Manage household expenses together. Track bills, split costs, and keep a record of shared expenses,
your personal shopping trips, and the general household budget. With AI-powered analysis of your receipts
to know where your money is going and what products you buy the most.

## Document Storage ##
Store important household documents in one secure place. From warranties to user manuals,
contracts to insurance papers, HomeHub keeps everything organized and easily accessible.

# About the Development #
## Architecture ##
HomeHub is built following the Clean Architecture principles. I tried to adhere to Domain-Driven Design (DDD) as much as possible.
The Git history might be a bit messy, I do not really care since I am the only one working on that project.

## Technologies ##
HomeHub is built using the following technologies:
- Classic SpringBoot with Spring Security
- Docker for containeritzation
- PostgreSQL for database, preferably via Supabase
- Testcontainers for integration tests
- JUnit 5 for unit tests
- GitHub Actions for CI/CD, Deployement to a VPS via SSH
- Grafana for monitoring
- SLF4J for logging
- Kubernetes for upscaling

All in all, this project is not really about someone actually using the finished product, but more about
learning the technologies and how to build a project like this. A separate frontend using Flutter is
planned to follow in the future.