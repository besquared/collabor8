require 'set'
require 'rubygems'
require 'fastercsv'
require 'ratings'
require 'ostruct'

my_id = '97457' # jordi

people = {}

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
  people[row[0]] = row[1]
  people[row[2]] = row[3]
  
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

# ratings.suggest(my_id, not_following, :neighborhood => my_followers).each do |person, suggestion|
#   puts "#{people[person]} => #{suggestion}"
# end

should_recommend = []
should_not_recommend = []
ratings.suggest(my_id, not_following, :user_neighborhood => my_followers, :item_neighborhood => my_followees).each do |person, suggestion|
  if suggestion.include?(Ratings::Like)
    should_recommend << people[person]
  else
    should_not_recommend << people[person]
  end
end

puts "We recommend:"
puts should_recommend.inspect

puts "We do not recommend:"
puts should_not_recommend.inspect