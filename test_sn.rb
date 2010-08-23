require 'set'
require 'rubygems'
require 'fastercsv'
require 'ratings'
require 'ostruct'

my_id = '97457'

actual = {}
my_followees = Set.new
my_followers = Set.new
all_followees = Set.new
my_declinations = Set.new

FasterCSV.open('declined.csv').each do |row|
  my_declinations = row[1] if row[0] == my_id
  actual["#{row[0]}:#{row[1]}"] = Ratings::Dislike
end

FasterCSV.open('followings.csv').each do |row|
  all_followees << row[2]
  my_followees << row[2] if row[0] == my_id
  my_followers << row[0] if row[2] == my_id
  actual["#{row[0]}:#{row[2]}"] = Ratings::Like
end

ratings = Ratings.new
actual.each do |users, rating|
  pair = users.split(':')
  ratings.add(pair.first, pair.last, rating)
end

not_following = all_followees - my_followees - my_declinations
puts ratings.suggest(my_id, not_following, :neighborhood => my_followers).inspect

# puts ratings.suggest("97113", )